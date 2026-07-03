import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class TestePreAtividade {

    private static final int N = 10_000;
    private static final int M = 1_000;

    public static void main(String[] args) {
        Random sorteio = new Random(42);

        Integer[] valores = new Integer[N];
        for (int i = 0; i < N; i++) {
            valores[i] = sorteio.nextInt();
        }

        Integer[] buscas = new Integer[M];
        for (int i = 0; i < M; i++) {
            buscas[i] = sorteio.nextInt();
        }

        ABB<Integer, Integer> abbAleatoria = new ABB<>();
        AVL<Integer, Integer> avlAleatoria = new AVL<>();
        for (Integer valor : valores) {
            abbAleatoria.inserir(valor, valor);
            avlAleatoria.inserir(valor, valor);
        }

        long comparacoesAbbAleatoria = 0;
        long comparacoesAvlAleatoria = 0;
        double tempoAbbAleatoria = 0;
        double tempoAvlAleatoria = 0;
        for (Integer busca : buscas) {
            comparacoesAbbAleatoria += pesquisarSeguro(abbAleatoria, busca);
            tempoAbbAleatoria += abbAleatoria.getTempo();
            comparacoesAvlAleatoria += pesquisarSeguro(avlAleatoria, busca);
            tempoAvlAleatoria += avlAleatoria.getTempo();
        }

        Integer[] valoresOrdenados = valores.clone();
        Arrays.sort(valoresOrdenados);

        ABB<Integer, Integer> abbOrdenada = new ABB<>();
        AVL<Integer, Integer> avlOrdenada = new AVL<>();
        for (Integer valor : valoresOrdenados) {
            abbOrdenada.inserir(valor, valor);
            avlOrdenada.inserir(valor, valor);
        }

        long comparacoesAbbOrdenada = 0;
        long comparacoesAvlOrdenada = 0;
        double tempoAbbOrdenada = 0;
        double tempoAvlOrdenada = 0;
        for (Integer busca : buscas) {
            comparacoesAbbOrdenada += pesquisarSeguro(abbOrdenada, busca);
            tempoAbbOrdenada += abbOrdenada.getTempo();
            comparacoesAvlOrdenada += pesquisarSeguro(avlOrdenada, busca);
            tempoAvlOrdenada += avlOrdenada.getTempo();
        }

        System.out.println("Estrutura / insercao\tTotal de comparacoes\tTempo total (ns)");
        System.out.println("ABB / aleatoria\t" + comparacoesAbbAleatoria + "\t" + tempoAbbAleatoria);
        System.out.println("AVL / aleatoria\t" + comparacoesAvlAleatoria + "\t" + tempoAvlAleatoria);
        System.out.println("ABB / ordenada\t" + comparacoesAbbOrdenada + "\t" + tempoAbbOrdenada);
        System.out.println("AVL / ordenada\t" + comparacoesAvlOrdenada + "\t" + tempoAvlOrdenada);

        System.out.println();
        System.out.println("1) Com insercao em ordem aleatoria a ABB tende a ficar proxima do balanceamento por"
                + " acaso, entao seu numero de comparacoes fica perto do da AVL.");
        System.out.println("2) Com insercao em ordem crescente a diferenca fica evidente: cada novo valor e sempre"
                + " maior que o anterior, entao a ABB degenera em uma lista encadeada (so cresce para a direita),"
                + " com busca O(n); a AVL se mantem balanceada por causa das rotacoes, com busca O(log n).");
        System.out.println("3) Como a ordem dos produtos no arquivo produtos.txt nao segue nenhum criterio"
                + " garantido, pode acontecer de os ids chegarem ja ordenados (ou proximos disso), o que faria uma"
                + " ABB comum degenerar. Usar AVL para produtosPorId garante desempenho O(log n) na busca"
                + " independentemente da ordem de insercao dos dados.");
    }

    private static long pesquisarSeguro(IMapeamento<Integer, Integer> arvore, Integer chave) {
        try {
            arvore.pesquisar(chave);
        } catch (NoSuchElementException excecao) {
            /// O valor nao esta na arvore, mas as comparacoes feitas ate essa conclusao ainda contam para a medicao.
        }
        return arvore.getComparacoes();
    }
}
