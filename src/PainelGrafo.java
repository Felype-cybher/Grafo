import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

public class PainelGrafo extends JPanel {

    public enum Modo {
        ADICIONAR_VERTICE, ADICIONAR_ARESTA, MOVER, NENHUM
    }

    private Modo modoAtual = Modo.ADICIONAR_VERTICE;
    private final List<Vertice> vertices = new ArrayList<>();
    private final List<Aresta> arestas = new ArrayList<>();
    private int contadorVertices = 0;

    // Variáveis para interatividade
    private Vertice verticeSelecionado = null;
    private Vertice verticeSobMouse = null;
    private Vertice verticeArrastado = null;
    private Point pontoArrastado = null;

    public static final int RAIO_VERTICE = 25;

    public PainelGrafo() {
        configurarListeners();
        setBackground(new Color(0x1E, 0x1E, 0x1E)); // Cor de fundo escura (Dark Mode)
    }

    public void setModo(Modo modo) {
        this.modoAtual = modo;
        verticeSelecionado = null; // Reseta a seleção ao mudar de modo
        repaint();
    }

    private void configurarListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                verticeArrastado = getVerticeEm(e.getPoint());
                if (modoAtual == Modo.MOVER && verticeArrastado != null) {
                    pontoArrastado = e.getPoint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Vertice clicado = getVerticeEm(e.getPoint());

                if (modoAtual == Modo.ADICIONAR_VERTICE && clicado == null) {
                    vertices.add(new Vertice(e.getPoint(), String.valueOf(contadorVertices++)));
                } else if (modoAtual == Modo.ADICIONAR_ARESTA && clicado != null) {
                    if (verticeSelecionado == null) {
                        verticeSelecionado = clicado;
                    } else {
                        if (!verticeSelecionado.equals(clicado)) {
                            arestas.add(new Aresta(verticeSelecionado, clicado));
                        }
                        verticeSelecionado = null;
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                verticeArrastado = null;
                pontoArrastado = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                verticeSobMouse = getVerticeEm(e.getPoint());
                pontoArrastado = e.getPoint(); // Para a linha de preview
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (modoAtual == Modo.MOVER && verticeArrastado != null) {
                    verticeArrastado.setPosicao(e.getPoint());
                    pontoArrastado = e.getPoint();
                    repaint();
                }
            }
        });
    }

    private Vertice getVerticeEm(Point p) {
        for (Vertice v : vertices) {
            if (v.getPosicao().distance(p) <= RAIO_VERTICE) {
                return v;
            }
        }
        return null;
    }

    public void limparTudo() {
        vertices.clear();
        arestas.clear();
        contadorVertices = 0;
        verticeSelecionado = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(0x60, 0x60, 0x60));
        for (Aresta a : arestas) {
            Point p1 = a.getV1().getPosicao();
            Point p2 = a.getV2().getPosicao();
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        if (modoAtual == Modo.ADICIONAR_ARESTA && verticeSelecionado != null && pontoArrastado != null) {
            Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            g2d.setStroke(dashed);
            g2d.setColor(Color.CYAN);
            Point p1 = verticeSelecionado.getPosicao();
            g2d.drawLine(p1.x, p1.y, pontoArrastado.x, pontoArrastado.y);
        }

        for (Vertice v : vertices) {
            Point p = v.getPosicao();
            int x = p.x - RAIO_VERTICE;
            int y = p.y - RAIO_VERTICE;
            int diametro = RAIO_VERTICE * 2;

            Color cor1 = new Color(0x00, 0x7A, 0xCC);
            Color cor2 = new Color(0x00, 0x5A, 0x99);
            g2d.setPaint(new GradientPaint(x, y, cor1, x + diametro, y + diametro, cor2));
            g2d.fillOval(x, y, diametro, diametro);

            if (v.equals(verticeSelecionado)) {
                g2d.setColor(Color.CYAN);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(x, y, diametro, diametro);
            } else if (v.equals(verticeSobMouse)) {
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x, y, diametro, diametro);
            }

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(v.getRotulo(), p.x - fm.stringWidth(v.getRotulo()) / 2, p.y + fm.getAscent() / 4);
        }
    }
}