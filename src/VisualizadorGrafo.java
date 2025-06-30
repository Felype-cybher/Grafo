import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VisualizadorGrafo {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Editor de Grafo Profissional");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 768);

            PainelGrafo painelGrafo = new PainelGrafo();
            JLabel statusBar = new JLabel("Modo: Adicionar Vértice. Clique na tela para adicionar.");

            JToolBar toolBar = new JToolBar();
            ButtonGroup modeGroup = new ButtonGroup();

            JToggleButton btnAddVertice = new JToggleButton("Adicionar Vértice", true);
            JToggleButton btnAddAresta = new JToggleButton("Adicionar Aresta");
            JToggleButton btnMover = new JToggleButton("Mover");


            modeGroup.add(btnAddVertice);
            modeGroup.add(btnAddAresta);
            modeGroup.add(btnMover);

            toolBar.add(btnAddVertice);
            toolBar.add(btnAddAresta);
            toolBar.add(btnMover);
            toolBar.add(Box.createHorizontalGlue()); 

            JButton btnLimpar = new JButton("Limpar Tudo");
            toolBar.add(btnLimpar);

            btnAddVertice.addActionListener(e -> {
                painelGrafo.setModo(PainelGrafo.Modo.ADICIONAR_VERTICE);
                statusBar.setText("Modo: Adicionar Vértice. Clique na tela para adicionar.");
            });
            btnAddAresta.addActionListener(e -> {
                painelGrafo.setModo(PainelGrafo.Modo.ADICIONAR_ARESTA);
                statusBar.setText("Modo: Adicionar Aresta. Clique em um vértice e depois em outro.");
            });
            btnMover.addActionListener(e -> {
                painelGrafo.setModo(PainelGrafo.Modo.MOVER);
                statusBar.setText("Modo: Mover. Clique e arraste um vértice.");
            });
            btnLimpar.addActionListener(e -> {
                painelGrafo.limparTudo();
                statusBar.setText("Tudo limpo. Modo: Adicionar Vértice.");
                btnAddVertice.setSelected(true);
            });

            frame.setLayout(new BorderLayout());
            frame.add(toolBar, BorderLayout.NORTH);
            frame.add(painelGrafo, BorderLayout.CENTER);
            frame.add(statusBar, BorderLayout.SOUTH);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
