package cl.mapuescuela.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.mapuescuela.model.Cliente;
import cl.mapuescuela.repository.ClienteRepository;

@Service
public class RecuperacionPasswordService {

    private static final String CARACTERES =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";

    private static final int LONGITUD_PASSWORD = 10;

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${spring.mail.username}")
    private String correoRemitente;

    public RecuperacionPasswordService(
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder,
            JavaMailSender mailSender) {

        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public boolean recuperarPassword(String email) {

        Cliente cliente = clienteRepository
                .findByEmailIgnoreCase(email.trim())
                .orElse(null);

        if (cliente == null) {
            return false;
        }

        String passwordTemporal = generarPasswordTemporal();

        /*
         * Primero enviamos el correo.
         *
         * Si Gmail genera un error, no modificamos la contraseña actual
         * del cliente y este puede continuar ingresando normalmente.
         */
        enviarCorreo(cliente, passwordTemporal);

        /*
         * Solamente después de que el envío fue aceptado se reemplaza
         * la contraseña por el hash de la contraseña temporal.
         */
        cliente.setPasswordHash(
                passwordEncoder.encode(passwordTemporal));

        clienteRepository.saveAndFlush(cliente);

        return true;
    }

    private String generarPasswordTemporal() {

        StringBuilder password = new StringBuilder();

        password.append("Mapu-");

        for (int i = 0; i < LONGITUD_PASSWORD; i++) {
            int posicion = secureRandom.nextInt(CARACTERES.length());
            password.append(CARACTERES.charAt(posicion));
        }

        return password.toString();
    }

    private void enviarCorreo(
            Cliente cliente,
            String passwordTemporal) {

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setFrom(correoRemitente);
        mensaje.setTo(cliente.getEmail());
        mensaje.setSubject(
                "MapuEscuela - Recuperación de contraseña");

        mensaje.setText(
                "Hola "
                + obtenerNombre(cliente)
                + ",\n\n"
                + "Se solicitó recuperar el acceso a su cuenta "
                + "de MapuEscuela.\n\n"
                + "Su nueva contraseña temporal es:\n\n"
                + passwordTemporal
                + "\n\n"
                + "Puede utilizar esta contraseña para iniciar sesión "
                + "en el Portal de Clientes.\n\n"
                + "Si usted no solicitó este cambio, comuníquese con "
                + "el administrador de MapuEscuela.\n\n"
                + "MapuEscuela");

        mailSender.send(mensaje);
    }

    private String obtenerNombre(Cliente cliente) {

        if (cliente.getNombreContacto() != null
                && !cliente.getNombreContacto().isBlank()) {

            return cliente.getNombreContacto().trim();
        }

        return cliente.getRazonSocial();
    }
}