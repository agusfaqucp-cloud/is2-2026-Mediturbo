import model.Rol;
import model.SistemaSesion;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginVentana extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    static final Color AZUL_OSCURO  = new Color(15, 40, 80);
    static final Color AZUL_MEDIO   = new Color(25, 70, 140);
    static final Color DORADO       = new Color(190, 155, 90);
    static final Color BLANCO_SUAVE = new Color(248, 250, 253);
    static final Color GRIS_TEXTO   = new Color(90, 100, 115);
    static final Color BORDE_SUAVE  = new Color(210, 220, 235);

    public LoginVentana() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("MediTurnos - Acceso al sistema");
        setSize(780, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(crearPanelIzquierdo());
        root.add(crearPanelDerecho());
        add(root);
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint grad = new GradientPaint(
                    0, 0, AZUL_OSCURO,
                    getWidth(), getHeight(), new Color(20, 60, 120)
                );
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(-80, -80, 350, 350);
                g2.fillOval(100, 250, 250, 250);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2 - 50;
                int arm = 28;
                int thick = 11;
                g2.setColor(new Color(190, 155, 90, 200));
                g2.fillRoundRect(cx - thick/2, cy - arm, thick, arm*2, 6, 6);
                g2.fillRoundRect(cx - arm, cy - thick/2, arm*2, thick, 6, 6);

                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(190, 155, 90, 140));
                g2.drawOval(cx - 50, cy - 50, 100, 100);

                g2.setColor(DORADO);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(40, getHeight() - 80, getWidth() - 40, getHeight() - 80);
            }
        };

        JLabel nombre = new JLabel("<html><center>Centro Medico<br><b>Cuenca Del Plata</b></center></html>", JLabel.CENTER);
        nombre.setFont(new Font("Georgia", Font.PLAIN, 20));
        nombre.setForeground(Color.WHITE);
        nombre.setBorder(BorderFactory.createEmptyBorder(200, 20, 0, 20));

        JLabel sub = new JLabel("Atencion medica de excelencia", JLabel.CENTER);
        sub.setFont(new Font("Georgia", Font.ITALIC, 12));
        sub.setForeground(new Color(190, 155, 90));
        sub.setBorder(BorderFactory.createEmptyBorder(8, 20, 60, 20));

        JPanel textos = new JPanel(new BorderLayout());
        textos.setOpaque(false);
        textos.add(nombre, BorderLayout.NORTH);
        textos.add(sub, BorderLayout.SOUTH);

        panel.add(textos, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BLANCO_SUAVE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 40, 0, 40);

        JLabel titulo = new JLabel("Iniciar sesion");
        titulo.setFont(new Font("Georgia", Font.BOLD, 24));
        titulo.setForeground(AZUL_OSCURO);
        gbc.gridy = 0; gbc.insets = new Insets(0, 40, 4, 40);
        panel.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Sistema de gestion de turnos");
        subtitulo.setFont(new Font("Georgia", Font.ITALIC, 13));
        subtitulo.setForeground(GRIS_TEXTO);
        gbc.gridy = 1; gbc.insets = new Insets(0, 40, 28, 40);
        panel.add(subtitulo, gbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(DORADO);
        sep.setPreferredSize(new Dimension(0, 2));
        gbc.gridy = 2; gbc.insets = new Insets(0, 40, 24, 40);
        panel.add(sep, gbc);

        gbc.insets = new Insets(0, 40, 6, 40);
        JLabel lblU = new JLabel("Usuario");
        lblU.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblU.setForeground(GRIS_TEXTO);
        gbc.gridy = 3;
        panel.add(lblU, gbc);

        txtUsuario = crearCampo("admin / paciente / medico");
        gbc.gridy = 4; gbc.insets = new Insets(0, 40, 16, 40);
        panel.add(txtUsuario, gbc);

        gbc.insets = new Insets(0, 40, 6, 40);
        JLabel lblP = new JLabel("Contrasena");
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblP.setForeground(GRIS_TEXTO);
        gbc.gridy = 5;
        panel.add(lblP, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(0, 40));
        txtPassword.setBorder(new CompoundBorder(
            new LineBorder(BORDE_SUAVE, 1, true),
            new EmptyBorder(6, 12, 6, 12)
        ));
        gbc.gridy = 6; gbc.insets = new Insets(0, 40, 24, 40);
        panel.add(txtPassword, gbc);

        JButton btn = new JButton("Ingresar al sistema") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(AZUL_OSCURO);
                } else if (getModel().isRollover()) {
                    g2.setColor(AZUL_MEDIO.brighter());
                } else {
                    g2.setColor(AZUL_MEDIO);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(0, 42));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7; gbc.insets = new Insets(0, 40, 20, 40);
        panel.add(btn, gbc);

        JLabel hint = new JLabel("contrasena: 1234", JLabel.CENTER);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(170, 175, 185));
        gbc.gridy = 8; gbc.insets = new Insets(0, 40, 0, 40);
        panel.add(hint, gbc);

        btn.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());
        txtUsuario.addActionListener(e -> txtPassword.requestFocusInWindow());

        return panel;
    }

    private JTextField crearCampo(String placeholder) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(180, 185, 195));
                    g2.setFont(new Font("Segoe UI", Font.ITALIC, 13));
                    g2.drawString(placeholder, 12, getHeight() / 2 + 5);
                }
            }
        };
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(0, 40));
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDE_SUAVE, 1, true),
            new EmptyBorder(6, 12, 6, 12)
        ));
        return f;
    }

    private void login() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (!pass.equals("1234")) {
            mostrarError();
            return;
        }

        Usuario usuario;
        switch (user) {
            case "admin":
                usuario = new Usuario("admin", Rol.ADMIN);
                break;
            case "paciente":
                usuario = new Usuario("paciente", Rol.PACIENTE);
                break;
            case "medico":
                usuario = new Usuario("medico", Rol.MEDICO);
                break;
            default:
                mostrarError();
                return;
        }

        SistemaSesion.setUsuarioActual(usuario);
        new VentanaPrincipal().setVisible(true);
        dispose();
    }

    private void mostrarError() {
        JOptionPane.showMessageDialog(this,
            "Usuario o contrasena incorrectos.",
            "Acceso denegado", JOptionPane.ERROR_MESSAGE);
        txtPassword.setText("");
        txtUsuario.requestFocusInWindow();
    }
}
