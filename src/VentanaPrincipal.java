import model.*;
import strategy.*;
import observer.ServicioNotificacion;
import datos.PersistenciaTurnos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private static final Color AZUL_OSCURO  = LoginVentana.AZUL_OSCURO;
    private static final Color AZUL_MEDIO   = LoginVentana.AZUL_MEDIO;
    private static final Color DORADO       = LoginVentana.DORADO;
    private static final Color BLANCO_SUAVE = LoginVentana.BLANCO_SUAVE;
    private static final Color GRIS_TEXTO   = LoginVentana.GRIS_TEXTO;
    private static final Color BORDE_SUAVE  = LoginVentana.BORDE_SUAVE;

    private static final Color VERDE_CONF  = new Color(34, 139, 80);
    private static final Color ROJO_CANC   = new Color(190, 45, 45);
    private static final Color AZUL_ATEND  = new Color(30, 100, 185);
    private static final Color FONDO_PANEL = new Color(240, 244, 252);

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final List<Turno> listaTurnos = new ArrayList<>();

    private final ServicioNotificacion servicioNotificacion = new ServicioNotificacion();
    private final DefaultListModel<String> modeloNotif = new DefaultListModel<>();

    private int cntPendiente = 0, cntConfirmado = 0, cntAtendido = 0;
    private JLabel lblCntPend, lblCntConf, lblCntAtend;

    public VentanaPrincipal() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("MediTurnos - Panel de gestion");
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(BLANCO_SUAVE);

        modeloTabla = new DefaultTableModel(
            new String[]{"#", "Paciente", "Especialidad", "Medico", "Estado", "Fecha", "Usuario"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = construirTabla();

        setLayout(new BorderLayout(0, 0));
        add(crearHeader(),      BorderLayout.NORTH);
        add(crearCentro(),      BorderLayout.CENTER);
        add(crearBarraEstado(), BorderLayout.SOUTH);

        servicioNotificacion.agregarUIListener(() ->
            SwingUtilities.invokeLater(() -> {
                String msg = servicioNotificacion.getUltimaNotificacion();
                modeloNotif.add(0, msg);
            })
        );

        cargarTurnosGuardados();
    }

    private void cargarTurnosGuardados() {
        List<Turno> guardados = PersistenciaTurnos.cargarJSON();
        for (Turno t : guardados) {
            listaTurnos.add(t);
            modeloTabla.addRow(new Object[]{
                listaTurnos.size(),
                t.getPaciente().getNombre(),
                t.getEspecialidad().getNombre(),
                t.getMedico().getNombre(),
                t.getEstado().toString(),
                t.getFecha(),
                t.getCreadoPor() != null ? t.getCreadoPor().getNombre() : "-"
            });
            Historial.agregarTurno(t);
            contarEstado(t.getEstado());
        }
        refrescarContadores();
    }

    private void contarEstado(Estado e) {
        if (e == Estado.PENDIENTE)  cntPendiente++;
        if (e == Estado.CONFIRMADO) cntConfirmado++;
        if (e == Estado.ATENDIDO)   cntAtendido++;
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, AZUL_OSCURO, getWidth(), 0, AZUL_MEDIO));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(DORADO);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
            }
        };
        header.setPreferredSize(new Dimension(0, 115));

        JPanel marca = new JPanel(new BorderLayout());
        marca.setOpaque(false);
        marca.setBorder(new EmptyBorder(16, 22, 16, 0));

        JPanel iconoPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DORADO);
                g2.fillOval(0, 0, 40, 40);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 22));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("+", (40 - fm.stringWidth("+")) / 2, (40 + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        iconoPanel.setPreferredSize(new Dimension(44, 44));
        iconoPanel.setOpaque(false);

        String rolStr = "";
        Usuario u = SistemaSesion.getUsuarioActual();
        if (u != null) rolStr = " [" + u.getRol().toString() + "]";

        JLabel nombre = new JLabel("  MediTurnos  -  Centro Medico Cuenca Del Plata");
        nombre.setFont(new Font("Georgia", Font.BOLD, 17));
        nombre.setForeground(Color.WHITE);

        JLabel usuario = new JLabel("  usuario: " + (u != null ? u.getNombre() + rolStr : "?"));
        usuario.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        usuario.setForeground(DORADO);

        JPanel marcaTxt = new JPanel(new GridLayout(2, 1));
        marcaTxt.setOpaque(false);
        marcaTxt.add(nombre);
        marcaTxt.add(usuario);

        JPanel marcaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        marcaRow.setOpaque(false);
        marcaRow.add(iconoPanel);
        marcaRow.add(marcaTxt);
        marca.add(marcaRow, BorderLayout.CENTER);

        JPanel contadores = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 22));
        contadores.setOpaque(false);
        contadores.setBorder(new EmptyBorder(0, 0, 0, 22));

        lblCntPend  = crearContador("PENDIENTES",  "0", new Color(230, 160, 30));
        lblCntConf  = crearContador("CONFIRMADOS", "0", new Color(60, 180, 100));
        lblCntAtend = crearContador("ATENDIDOS",   "0", new Color(80, 160, 220));

        contadores.add(lblCntPend.getParent());
        contadores.add(lblCntConf.getParent());
        contadores.add(lblCntAtend.getParent());

        header.add(marca,      BorderLayout.WEST);
        header.add(contadores, BorderLayout.EAST);
        return header;
    }

    private JLabel crearContador(String etiqueta, String valor, Color color) {
        JPanel box = new JPanel(new BorderLayout(0, 2)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 22));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        box.setOpaque(false);
        box.setBorder(new EmptyBorder(8, 16, 8, 16));

        JLabel num = new JLabel(valor, JLabel.CENTER);
        num.setFont(new Font("Georgia", Font.BOLD, 28));
        num.setForeground(color);

        JLabel lbl = new JLabel(etiqueta, JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(new Color(200, 210, 230));

        box.add(num, BorderLayout.CENTER);
        box.add(lbl, BorderLayout.SOUTH);
        return num;
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(0, 0));
        centro.setBackground(BLANCO_SUAVE);

        boolean esAdmin  = esRol(Rol.ADMIN);
        boolean esMedico = esRol(Rol.MEDICO);

        if (esAdmin || esMedico) {
            centro.add(crearSidebarIzquierdo(), BorderLayout.WEST);
        }

        JPanel derecha = new JPanel(new BorderLayout(0, 0));
        derecha.setBackground(BLANCO_SUAVE);
        derecha.add(crearToolbar(), BorderLayout.NORTH);
        derecha.add(crearAreaTabla(), BorderLayout.CENTER);

        centro.add(derecha, BorderLayout.CENTER);
        return centro;
    }

    private JPanel crearSidebarIzquierdo() {
        JPanel side = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, AZUL_OSCURO, 0, getHeight(), new Color(20, 55, 110)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillOval(-30, getHeight() / 2, 160, 160);
            }
        };
        side.setPreferredSize(new Dimension(190, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(24, 0, 0, 0));

        JLabel lblMenu = new JLabel("  Menu");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenu.setForeground(new Color(160, 175, 210));
        lblMenu.setBorder(new EmptyBorder(0, 18, 10, 0));
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(lblMenu);

        side.add(sideBtn("Nuevo turno",    DORADO));
        side.add(Box.createVerticalStrut(4));
        side.add(sideBtn("Confirmar",       new Color(34, 139, 80)));
        side.add(Box.createVerticalStrut(4));
        side.add(sideBtn("Marcar atendido", AZUL_ATEND));
        side.add(Box.createVerticalStrut(4));
        side.add(sideBtn("Cancelar",        ROJO_CANC));
        side.add(Box.createVerticalStrut(20));

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(255, 255, 255, 30));
        sep2.setMaximumSize(new Dimension(160, 1));
        sep2.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(sep2);
        side.add(Box.createVerticalStrut(14));

        side.add(sideBtn("Historial",       new Color(140, 150, 175)));
        side.add(Box.createVerticalStrut(4));
        side.add(sideBtn("Ver todos los turnos", new Color(90, 130, 200)));
        side.add(Box.createVerticalStrut(4));
        side.add(sideBtn("Guardar turnos",  new Color(80, 160, 110)));
        side.add(Box.createVerticalGlue());

        return side;
    }

    private JButton sideBtn(String texto, Color acento) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed()  ? acento.darker() :
                             getModel().isRollover() ? new Color(255,255,255,28) :
                             new Color(255,255,255,12);
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2.setColor(acento);
                    g2.fillRoundRect(0, 0, 4, getHeight(), 3, 3);
                }
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 16, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setMaximumSize(new Dimension(190, 38));
        btn.setPreferredSize(new Dimension(190, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            switch (texto) {
                case "Confirmar"        -> cambiarEstado(Estado.CONFIRMADO);
                case "Marcar atendido"  -> cambiarEstado(Estado.ATENDIDO);
                case "Cancelar"         -> cambiarEstado(Estado.CANCELADO);
                case "Historial"        -> new VentanaHistorial().setVisible(true);
                case "Ver todos los turnos" -> new VentanaGestionTurnos(listaTurnos).setVisible(true);
                case "Guardar turnos"   -> guardarTurnos();
                case "Nuevo turno"      -> mostrarDialogoNuevoTurno();
            }
        });

        return btn;
    }

    private JPanel crearToolbar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bar.setBackground(new Color(232, 238, 252));
        bar.setBorder(new MatteBorder(0, 0, 1, 0, BORDE_SUAVE));

        boolean puedeCrear = esRol(Rol.ADMIN) || esRol(Rol.PACIENTE);

        if (puedeCrear) {
            JTextField txtNombre = new JTextField(14);
            txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtNombre.setPreferredSize(new Dimension(180, 34));
            txtNombre.setBorder(new CompoundBorder(
                new LineBorder(BORDE_SUAVE, 1, true),
                new EmptyBorder(4, 10, 4, 10)
            ));

            JComboBox<String> comboEsp = new JComboBox<>(
                new String[]{"Cardiologia", "Pediatria", "Dermatologia"});
            comboEsp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            comboEsp.setPreferredSize(new Dimension(150, 34));

            JButton btnCrear = toolBtn("Nuevo Turno", AZUL_MEDIO);

            bar.add(new JLabel("  Paciente:"));
            bar.add(txtNombre);
            bar.add(comboEsp);
            bar.add(Box.createHorizontalStrut(6));
            bar.add(btnCrear);

            btnCrear.addActionListener(e -> {
                String nombre = txtNombre.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Ingresa el nombre del paciente.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                crearTurno(nombre, comboEsp.getSelectedItem().toString());
                txtNombre.setText("");
            });
        }

        if (esRol(Rol.ADMIN)) {
            JButton btnConfirmar = toolBtn("Confirmar",  VERDE_CONF);
            JButton btnAtendido  = toolBtn("Atendido",   AZUL_ATEND);
            JButton btnCancelar  = toolBtn("Cancelar",   ROJO_CANC);
            JButton btnHistorial = toolBtn("Historial",  new Color(90, 90, 100));
            JButton btnGestion   = toolBtn("Gestion",    new Color(60, 100, 180));
            JButton btnGuardar   = toolBtn("Guardar",    new Color(60, 140, 90));

            bar.add(Box.createHorizontalStrut(6));
            bar.add(btnConfirmar);
            bar.add(btnAtendido);
            bar.add(btnCancelar);
            bar.add(Box.createHorizontalStrut(6));
            bar.add(btnHistorial);
            bar.add(btnGestion);
            bar.add(btnGuardar);

            btnConfirmar.addActionListener(e -> cambiarEstado(Estado.CONFIRMADO));
            btnAtendido .addActionListener(e -> cambiarEstado(Estado.ATENDIDO));
            btnCancelar .addActionListener(e -> cambiarEstado(Estado.CANCELADO));
            btnHistorial.addActionListener(e -> new VentanaHistorial().setVisible(true));
            btnGestion  .addActionListener(e -> new VentanaGestionTurnos(listaTurnos).setVisible(true));
            btnGuardar  .addActionListener(e -> guardarTurnos());
        }

        if (esRol(Rol.MEDICO)) {
            JButton btnAtendido  = toolBtn("Atendido", AZUL_ATEND);
            JButton btnGestion   = toolBtn("Gestion",  new Color(60, 100, 180));
            bar.add(btnAtendido);
            bar.add(btnGestion);
            btnAtendido.addActionListener(e -> cambiarEstado(Estado.ATENDIDO));
            btnGestion .addActionListener(e -> new VentanaGestionTurnos(listaTurnos).setVisible(true));
        }

        return bar;
    }

    private void mostrarDialogoNuevoTurno() {
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        JTextField txtNombre = new JTextField();
        JComboBox<String> comboEsp = new JComboBox<>(
            new String[]{"Cardiologia", "Pediatria", "Dermatologia"});
        form.add(new JLabel("Paciente:"));
        form.add(txtNombre);
        form.add(new JLabel("Especialidad:"));
        form.add(comboEsp);

        int res = JOptionPane.showConfirmDialog(this, form, "Nuevo turno",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            if (!nombre.isEmpty()) {
                crearTurno(nombre, comboEsp.getSelectedItem().toString());
            }
        }
    }

    private void crearTurno(String nombrePaciente, String especialidadNombre) {
        Paciente paciente    = new Paciente(nombrePaciente);
        Especialidad esp     = new Especialidad(especialidadNombre);
        EstrategiaAsignacion estrategia = new AsignacionPorDisponibilidad();
        GestorTurnos gestor  = new GestorTurnos(estrategia, servicioNotificacion);
        Turno turno          = gestor.crearTurno(paciente, esp);

        listaTurnos.add(turno);
        modeloTabla.addRow(new Object[]{
            listaTurnos.size(),
            paciente.getNombre(),
            esp.getNombre(),
            turno.getMedico().getNombre(),
            turno.getEstado().toString(),
            turno.getFecha(),
            SistemaSesion.getUsuarioActual().getNombre()
        });
        Historial.agregarTurno(turno);
        cntPendiente++;
        refrescarContadores();
    }

    private JPanel crearAreaTabla() {
        JPanel area = new JPanel(new BorderLayout(0, 0));
        area.setBackground(BLANCO_SUAVE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new EmptyBorder(10, 12, 8, 0));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel panelNotif = new JPanel(new BorderLayout());
        panelNotif.setPreferredSize(new Dimension(240, 0));
        panelNotif.setBackground(FONDO_PANEL);
        panelNotif.setBorder(new MatteBorder(0, 1, 0, 0, BORDE_SUAVE));

        JPanel notifHeader = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, AZUL_OSCURO, getWidth(), 0, AZUL_MEDIO));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        notifHeader.setPreferredSize(new Dimension(0, 36));

        JLabel lblNotif = new JLabel("  Notificaciones");
        lblNotif.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNotif.setForeground(Color.WHITE);
        notifHeader.add(lblNotif, BorderLayout.CENTER);

        JLabel lblObs = new JLabel("Observer  ", JLabel.RIGHT);
        lblObs.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblObs.setForeground(DORADO);
        notifHeader.add(lblObs, BorderLayout.EAST);

        JList<String> listaNotif = new JList<>(modeloNotif);
        listaNotif.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        listaNotif.setBackground(FONDO_PANEL);
        listaNotif.setFixedCellHeight(42);
        listaNotif.setCellRenderer(new NotifCellRenderer());

        JScrollPane scrollNotif = new JScrollPane(listaNotif);
        scrollNotif.setBorder(BorderFactory.createEmptyBorder());

        panelNotif.add(notifHeader,  BorderLayout.NORTH);
        panelNotif.add(scrollNotif,  BorderLayout.CENTER);

        area.add(scroll,     BorderLayout.CENTER);
        area.add(panelNotif, BorderLayout.EAST);
        return area;
    }

    private JTable construirTabla() {
        JTable t = new JTable(modeloTabla);
        t.setRowHeight(32);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setSelectionBackground(new Color(210, 225, 255));
        t.setSelectionForeground(AZUL_OSCURO);
        t.setGridColor(new Color(228, 233, 245));
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(220, 228, 248));
        t.getTableHeader().setForeground(AZUL_OSCURO);
        t.getTableHeader().setPreferredSize(new Dimension(0, 36));
        t.getColumnModel().getColumn(0).setPreferredWidth(32);
        t.getColumnModel().getColumn(0).setMaxWidth(42);

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tbl, v, sel, foc, r, c);
                if (!sel) {
                    String estado = tbl.getValueAt(r, 4).toString();
                    switch (estado) {
                        case "CONFIRMADO" -> {
                            comp.setBackground(new Color(230, 252, 238));
                            comp.setForeground(c == 4 ? VERDE_CONF : Color.DARK_GRAY);
                        }
                        case "PENDIENTE" -> {
                            comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(246, 249, 255));
                            comp.setForeground(Color.DARK_GRAY);
                        }
                        case "CANCELADO" -> {
                            comp.setBackground(new Color(255, 235, 235));
                            comp.setForeground(c == 4 ? ROJO_CANC : new Color(150, 80, 80));
                        }
                        case "ATENDIDO" -> {
                            comp.setBackground(new Color(230, 242, 255));
                            comp.setForeground(c == 4 ? AZUL_ATEND : Color.DARK_GRAY);
                        }
                        default -> {
                            comp.setBackground(Color.WHITE);
                            comp.setForeground(Color.DARK_GRAY);
                        }
                    }
                    setFont(c == 4
                        ? new Font("Segoe UI", Font.BOLD, 12)
                        : new Font("Segoe UI", Font.PLAIN, 13));
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return comp;
            }
        });
        return t;
    }

    private JPanel crearBarraEstado() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(228, 234, 250));
        bar.setBorder(new MatteBorder(1, 0, 0, 0, BORDE_SUAVE));
        bar.setPreferredSize(new Dimension(0, 26));

        JLabel lbl = new JLabel("  Sistema listo  -  MediTurnos v1.0");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(GRIS_TEXTO);

        JLabel patron = new JLabel("Patrones: Strategy + Observer  ");
        patron.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        patron.setForeground(DORADO);

        bar.add(lbl,    BorderLayout.WEST);
        bar.add(patron, BorderLayout.EAST);
        return bar;
    }

    private void cambiarEstado(Estado nuevoEstado) {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un turno de la tabla.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Estado anterior = listaTurnos.get(fila).getEstado();
        listaTurnos.get(fila).cambiarEstado(nuevoEstado);
        modeloTabla.setValueAt(nuevoEstado.toString(), fila, 4);
        tabla.repaint();
        actualizarContadores(nuevoEstado, anterior);
    }

    private void actualizarContadores(Estado nuevo, Estado anterior) {
        if (nuevo == Estado.PENDIENTE)  cntPendiente++;
        if (nuevo == Estado.CONFIRMADO) {
            cntConfirmado++;
            if (anterior == Estado.PENDIENTE) cntPendiente = Math.max(0, cntPendiente - 1);
        }
        if (nuevo == Estado.ATENDIDO) {
            cntAtendido++;
            if (anterior == Estado.CONFIRMADO) cntConfirmado = Math.max(0, cntConfirmado - 1);
        }
        if (nuevo == Estado.CANCELADO) {
            if (anterior == Estado.PENDIENTE)  cntPendiente  = Math.max(0, cntPendiente  - 1);
            if (anterior == Estado.CONFIRMADO) cntConfirmado = Math.max(0, cntConfirmado - 1);
        }
        refrescarContadores();
    }

    private void refrescarContadores() {
        lblCntPend .setText(String.valueOf(cntPendiente));
        lblCntConf .setText(String.valueOf(cntConfirmado));
        lblCntAtend.setText(String.valueOf(cntAtendido));
    }

    private void guardarTurnos() {
        PersistenciaTurnos.guardarJSON(listaTurnos);
        PersistenciaTurnos.guardarTXT(listaTurnos);
        JOptionPane.showMessageDialog(this,
            "Turnos guardados en turnos.json y turnos.txt",
            "Guardado", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean esRol(Rol rol) {
        Usuario u = SistemaSesion.getUsuarioActual();
        return u != null && u.getRol() == rol;
    }

    private JButton toolBtn(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()  ? color.darker() :
                            getModel().isRollover() ? color.brighter() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 7, 7);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(
            new Font("Segoe UI", Font.BOLD, 12)
                .getStringBounds(texto, new java.awt.font.FontRenderContext(null, true, true))
                .getBounds().width + 28, 34));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    static class NotifCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
            lbl.setBorder(new CompoundBorder(
                new MatteBorder(0, 3, 1, 0,
                    index == 0 ? LoginVentana.DORADO : new Color(180, 190, 210)),
                new EmptyBorder(4, 8, 4, 4)
            ));
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            if (!isSelected) lbl.setBackground(index % 2 == 0
                ? new Color(243, 246, 255) : new Color(234, 240, 252));
            return lbl;
        }
    }
}
