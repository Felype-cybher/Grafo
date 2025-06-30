import java.awt.Point;

public class Vertice {
    private Point posicao;
    private String rotulo;

    public Vertice(Point posicao, String rotulo) {
        this.posicao = posicao;
        this.rotulo = rotulo;
    }

    public Point getPosicao() {
        return posicao;
    }

    public void setPosicao(Point posicao) {
        this.posicao = posicao;
    }

    public String getRotulo() {
        return rotulo;
    }
}