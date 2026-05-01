import model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class VentanaHistorial extends JFrame {

    private static final Color AZUL_OSCURO  = LoginVentana.AZUL_OSCURO;
    private static final Color AZUL_MEDIO   = LoginVentana.AZUL_MEDIO;
    private static final Color DORADO       = LoginVentana.DORADO;
    private static final Color BLANCO_SUAVE = LoginVentana.BLANCO_SUAVE;
    private static final Color BORDE_SUAVE  = LoginVentana.BORDE_SUAVE;

    public VentanaHistorial() {
        setTitle("MediTurnos - Historial de turnos");
        setSize(760, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(BLANCO_SUAVE);

        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, AZUL_OSCURO, getWidth(), 0, AZUL_MEDIO));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(DORADO);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
            }
        };
        header.setPreferredSize(new Dimension(0, 52));

        JLabel titulo = new JLabel("  Historial completo de turnos");
        titulo.setFont(new Font("Georgia", Font.BOLD, 16));
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.CENTER);

        List<Turno> turnos = Historial.getTurnos();
        JLabel totalLbl = new JLabel("Total: " + turnos.size() + " turno(s)   ");
        totalLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        totalLbl.setForeground(DORADO);
        header.add(totalLbl, BorderLayout.EAST);

        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"#", "Paciente", "Especialidad", "Medico", "Estado", "Fecha", "Creado por"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setGridColor(new Color(228, 233, 245));
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(220, 228, 248));
        tabla.getTableHeader().setForeground(AZUL_OSCURO);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 32));
        tabla.getColumnModel().getColumn(0).setMaxWidth(38);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    String estado = t.getValueAt(r, 4).toString();
                    comp.setBackground(switch (estado) {
                        case "CONFIRMADO" -> new Color(230, 252, 238);
                        case "CANCELADO"  -> new Color(255, 235, 235);
                        case "ATENDIDO"   -> new Color(230, 242, 255);
                        default           -> r % 2 == 0 ? Color.WHITE : new Color(246, 249, 255);
                    });
                    if (c == 4) {
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                        comp.setForeground(switch (estado) {
                            case "CONFIRMADO" -> new Color(34, 139, 80);
                            case "CANCELADO"  -> new Color(190, 45, 45);
                            case "ATENDIDO"   -> new Color(30, 100, 185);
                            default           -> Color.DARK_GRAY;
                        });
                    } else {
                        setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        comp.setForeground(Color.DARK_GRAY);
                    }
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return comp;
            }
        });

        int i = 1;
        for (Turno t : turnos) {
            modelo.addRow(new Object[]{
                i++,
                t.getPaciente().getNombre(),
                t.getEspecialidad().getNombre(),
                t.getMedico().getNombre(),
                t.getEstado().toString(),
                t.getFecha(),
                t.getCreadoPor() != null ? t.getCreadoPor().getNombre() : "-"
            });
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new EmptyBorder(6, 8, 6, 8));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(228, 234, 250));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, BORDE_SUAVE));
        footer.setPreferredSize(new Dimension(0, 26));
        JLabel footerLbl = new JLabel("  MediTurnos - Centro Medico San Rafael");
        footerLbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        footerLbl.setForeground(new Color(120, 130, 150));
        footer.add(footerLbl, BorderLayout.WEST);

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
        add(footer,  BorderLayout.SOUTH);
    }
}
