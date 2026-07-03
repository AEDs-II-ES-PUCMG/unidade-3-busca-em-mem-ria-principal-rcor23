import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuscaAleatoria<T> implements IBuscador<T> {

    private long comparacoes;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private T[] dados;

    public BuscaAleatoria(T[] dados){
        this.dados = dados;
    }

    @Override
    public long getComparacoes() {
        return comparacoes;
    }

    @Override
    public double getTempo() {
        if(inicio==null)
            throw new IllegalStateException("Não foi feita nenhuma busca.");

        return Duration.between(inicio, fim).toNanos();
    }

    @Override
    public T buscar(T dado) {
        comparacoes = 0;
        T encontrado = null;

        List<Integer> posicoes = new ArrayList<>();
        for (int i = 0; i < dados.length; i++) {
            posicoes.add(i);
        }
        Collections.shuffle(posicoes);

        inicio = LocalDateTime.now();
        int i = 0;
        while (i < posicoes.size() && encontrado == null) {
            comparacoes++;
            int pos = posicoes.get(i);
            if (dados[pos].equals(dado)) {
                encontrado = dados[pos];
            }
            i++;
        }
        fim = LocalDateTime.now();
        return encontrado;
    }

}
