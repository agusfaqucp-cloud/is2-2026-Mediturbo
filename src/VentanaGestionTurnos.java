import model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class VentanaGestionTurnos extends JFrame {

    private static final Color AZUL_OSCURO  = LoginVentana.AZUL_OSCURO;
    private static final Color AZUL_MEDIO   = LoginVentana.AZUL_MEDIO;
    private static final Color DORADO       = LoginVentana.DORADO;
    private static final Color BLANCO_SUAVE = LoginVentana.BLANCO_SUAVE;
    private static final Color GRIS_TEXTO   = LoginVentana.GRIS_TEXTO;
    private static final Color BORDE_SUAVE  = LoginVentana.BORDE_SUAVE;

    private static final Color VERDE_CONF  = new Color(34, 139, 80);
    private static final Color ROJO_CANC   = new Color(190, 45, 45);
    private static final Color AZUL_ATEND  = new Color(30, 100, 185);

    private final List<Turno> turnosOriginales;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    private JTextField txtFiltroFecha;
    private JTextField txtFiltroMedico;
    private JComboBox<String> comboFiltroEstado;

    public VentanaGestionTurnos(List<Turno> turnos) {
        this.turnosOriginales = turnos;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("MediTurnos - Gestion de turnos");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(BLANCO_SUAVE);

        modeloTabla = new DefaultTableModel(
            new String[]{"#", "Paciente", "Especialidad", "Medico", "Estado", "Fecha", "Creado por"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = construirTabla();

        setLayout(new BorderLayout(0, 0));
        add(crearHeader(),   BorderLayout.NORTH);
        add(crearFiltros(),  BorderLayout.CENTER);
        add(crearFooter(),   BorderLayout.SOUTH);

        cargarTodos();
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
        header.setPreferredSize(new Dimension(0, 52));

        JLabel titulo = new JLabel("  Gestion de Turnos");
        titulo.setFont(new Font("Georgia", Font.BOLD, 16));
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.CENTER);

        JLabel sub = new JLabel("Total: " + turnosOriginales.size() + " turno(s)   ");
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        sub.setForeground(DORADO);
        header.add(sub, BorderLayout.EAST);

        return header;
    }

    private JPanel crearFiltros() {
        JPanel contenedor = new JPanel(new BorderLayout(0, 0));
        contenedor.setBackground(BLANCO_SUAVE);

        JPanel barFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        barFiltros.setBackground(new Color(232, 238, 252));
        barFiltros.setBorder(new MatteBorder(0, 0, 1, 0, BORDE_SUAVE));

        JLabel lblF = new JLabel("Filtrar:");
        lblF.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblF.setForeground(AZUL_OSCURO);

        txtFiltroFecha = campoPeq("Fecha  (ej: 07/04)");
        txtFiltroMedico = campoPeq("Medico");
        comboFiltroEstado = new JComboBox<>(
            new String[]{"Todos", "PENDIENTE", "CONFIRMADO", "CANCELADO", "ATENDIDO"});
        comboFiltroEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboFiltroEstado.setPreferredSize(new Dimension(130, 34));

        JButton btnFiltrar = accionBtn("Aplicar filtro", AZUL_MEDIO);
        JButton btnLimpiar = accionBtn("Limpiar",        new Color(100, 110, 130));

        barFiltros.add(lblF);
        barFiltros.add(new JLabel("Fecha:"));
        barFiltros.add(txtFiltroFecha);
        barFiltros.add(new JLabel("Medico:"));
        barFiltros.add(txtFiltroMedico);
        barFiltros.add(new JLabel("Estado:"));
        barFiltros.add(comboFiltroEstado);
        barFiltros.add(Box.createHorizontalStrut(8));
        barFiltros.add(btnFiltrar);
        barFiltros.add(btnLimpiar);

        btnFiltrar.addActionListener(e -> aplicarFiltros());
        btnLimpiar.addActionListener(e -> limpiarFiltros());

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new EmptyBorder(8, 10, 8, 10));
        scroll.getViewport().setBackground(Color.WHITE);

        contenedor.add(barFiltros, BorderLayout.NORTH);
        contenedor.add(scroll,     BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearFooter() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(228, 234, 250));
        bar.setBorder(new MatteBorder(1, 0, 0, 0, BORDE_SUAVE));
        bar.setPreferredSize(new Dimension(0, 26));

        JLabel lbl = new JLabel("  MediTurnos - Centro Medico San Rafael");
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lbl.setForeground(GRIS_TEXTO);
        bar.add(lbl, BorderLayout.WEST);
        return bar;
    }

    private void cargarTodos() {
        modeloTabla.setRowCount(0);
        int i = 1;
        for (Turno t : turnosOriginales) {
            modeloTabla.addRow(fila(i++, t));
        }
    }

    private void aplicarFiltros() {
        String filtroFecha  = txtFiltroFecha.getText().trim().toLowerCase();
        String filtroMedico = txtFiltroMedico.getText().trim().toLowerCase();
        String filtroEstado = comboFiltroEstado.getSelectedItem().toString();

        modeloTabla.setRowCount(0);
        int i = 1;
        for (Turno t : turnosOriginales) {
            boolean matchFecha  = filtroFecha.isEmpty()  || t.getFecha().toLowerCase().contains(filtroFecha);
            boolean matchMedico = filtroMedico.isEmpty() || t.getMedico().getNombre().toLowerCase().contains(filtroMedico);
            boolean matchEstado = filtroEstado.equals("Todos") || t.getEstado().toString().equals(filtroEstado);
            if (matchFecha && matchMedico && matchEstado) {
                modeloTabla.addRow(fila(i++, t));
            }
        }
    }

    private void limpiarFiltros() {
        txtFiltroFecha.setText("");
        txtFiltroMedico.setText("");
        comboFiltroEstado.setSelectedIndex(0);
        cargarTodos();
    }

    private Object[] fila(int n, Turno t) {
        return new Object[]{
            n,
            t.getPaciente().getNombre(),
            t.getEspecialidad().getNombre(),
            t.getMedico().getNombre(),
            t.getEstado().toString(),
            t.getFecha(),
            t.getCreadoPor() != null ? t.getCreadoPor().getNombre() : "-"
        };
    }

    private JTable construirTabla() {
        JTable t = new JTable(modeloTabla);
        t.setRowHeight(30);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setSelectionBackground(new Color(210, 225, 255));
        t.setSelectionForeground(AZUL_OSCURO);
        t.setGridColor(new Color(228, 233, 245));
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(220, 228, 248));
        t.getTableHeader().setForeground(AZUL_OSCURO);
        t.getTableHeader().setPreferredSize(new Dimension(0, 34));
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

    private JTextField campoPeq(String placeholder) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(180, 185, 195));
                    g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                    g2.drawString(placeholder, 8, getHeight() / 2 + 5);
                }
            }
        };
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(130, 34));
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDE_SUAVE, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        return f;
    }

    private JButton accionBtn(String texto, Color color) {
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
}
