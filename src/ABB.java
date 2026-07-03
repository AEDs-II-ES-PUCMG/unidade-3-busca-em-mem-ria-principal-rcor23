import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ABB<K, V> implements IMapeamento<K, V>{

	private No<K, V> raiz; // referência à raiz da árvore.
	private Comparator<K> comparador; //comparador empregado para definir "menores" e "maiores".
	private int tamanho;
	private long comparacoes;
	private long inicio;
	private long termino;
	
	/**
	 * Método auxiliar para inicialização da árvore binária de busca.
	 * 
	 * Este método define a raiz da árvore como {@code null} e seu tamanho como 0.
	 * Utiliza o comparador fornecido para definir a organização dos elementos na árvore.
	 * @param comparador o comparador para organizar os elementos da árvore.
	 */
	private void init(Comparator<K> comparador) {
		raiz = null;
		tamanho = 0;
		this.comparador = comparador;
	}

	/**
	 * Construtor da classe.
	 * O comparador padrão de ordem natural será utilizado.
	 */ 
	@SuppressWarnings("unchecked")
	public ABB() {
	    init((Comparator<K>) Comparator.naturalOrder());
	}

	/**
	 * Construtor da classe.
	 * Esse construtor cria uma nova árvore binária de busca vazia.
	 *  
	 * @param comparador o comparador a ser utilizado para organizar os elementos da árvore.  
	 */
	public ABB(Comparator<K> comparador) {
	    init(comparador);
	}

    /**
     * Construtor da classe.
     * Esse construtor cria uma nova árvore binária de busca a partir de uma outra árvore binária de busca,
     * com os mesmos itens, mas usando uma nova chave.
     * @param original a árvore binária de busca original.
     * @param funcaoChave a função que irá extrair a nova chave de cada item para a nova árvore.
     */
    @SuppressWarnings("unchecked")
	public ABB(ABB<?, V> original, Function<V, K> funcaoChave) {
        ABB<K, V> nova = new ABB<>();
        nova = copiarArvore(original.raiz, funcaoChave, nova);
        this.raiz = nova.raiz;
        this.comparador = (Comparator<K>) Comparator.naturalOrder();
    }
    
    /**
     * Recursivamente, copia os elementos da árvore original para esta, num processo análogo ao caminhamento em ordem.
     * @param <T> Tipo da nova chave.
     * @param raizArvore raiz da árvore original que será copiada.
     * @param funcaoChave função extratora da nova chave para cada item da árvore.
     * @param novaArvore Nova árvore. Parâmetro usado para permitir o retorno da recursividade.
     * @return A nova árvore com os itens copiados e usando a chave indicada pela função extratora.
     */
    private <T> ABB<T, V> copiarArvore(No<?, V> raizArvore, Function<V, T> funcaoChave, ABB<T, V> novaArvore) {
    	
        if (raizArvore != null) {
    		novaArvore = copiarArvore(raizArvore.getEsquerda(), funcaoChave, novaArvore);
            V item = raizArvore.getItem();
            T chave = funcaoChave.apply(item);
    		novaArvore.inserir(chave, item);
    		novaArvore = copiarArvore(raizArvore.getDireita(), funcaoChave, novaArvore);
    	}
        return novaArvore;
    }
    
    /**
	 * Método booleano que indica se a árvore está vazia ou não.
	 * @return
	 * verdadeiro: se a raiz da árvore for null, o que significa que a árvore está vazia.
	 * falso: se a raiz da árvore não for null, o que significa que a árvore não está vazia.
	 */
	public Boolean vazia() {
	    return (this.raiz == null);
	}
    
    @Override
    /**
     * Método que encapsula a pesquisa recursiva de itens na árvore.
     * @param chave a chave do item que será pesquisado na árvore.
     * @return o valor associado à chave.
     */
	public V pesquisar(K chave) {
    	comparacoes = 0;
    	inicio = System.nanoTime();
    	V procurado = pesquisar(raiz, chave);
    	termino = System.nanoTime();
    	return procurado;
	}
    
    private V pesquisar(No<K, V> raizArvore, K procurado) {
    	
    	int comparacao;
    	
    	comparacoes++;
    	if (raizArvore == null)
    		/// Se a raiz da árvore ou sub-árvore for null, a árvore/sub-árvore está vazia e então o item não foi encontrado.
    		throw new NoSuchElementException("O item não foi localizado na árvore!");
    	
    	comparacao = comparador.compare(procurado, raizArvore.getChave());
    	
    	if (comparacao == 0)
    		/// O item procurado foi encontrado.
    		return raizArvore.getItem();
    	else if (comparacao < 0)
    		/// Se o item procurado for menor do que o item armazenado na raiz da árvore:
            /// pesquise esse item na sub-árvore esquerda.    
    		return pesquisar(raizArvore.getEsquerda(), procurado);
    	else
    		/// Se o item procurado for maior do que o item armazenado na raiz da árvore:
            /// pesquise esse item na sub-árvore direita.
    		return pesquisar(raizArvore.getDireita(), procurado);
    }
    
    @Override
    /**
     * Método que encapsula a adição recursiva de itens à árvore, associando-o à chave fornecida.
     * @param chave a chave associada ao item que será inserido na árvore.
     * @param item o item que será inserido na árvore.
     * 
     * @return o tamanho atualizado da árvore após a execução da operação de inserção.
     */
    public int inserir(K chave, V item) {
    	comparacoes = 0;
    	inicio = System.nanoTime();
    	raiz = inserir(raiz, chave, item);
    	tamanho++;
    	termino = System.nanoTime();
    	return tamanho;
    }

    private No<K, V> inserir(No<K, V> raizArvore, K chave, V item) {

    	if (raizArvore == null)
    		/// Se a raiz da árvore ou sub-árvore for null, a árvore/sub-árvore está vazia e então um novo item é inserido.
    		return new No<>(chave, item);

    	comparacoes++;
    	int comparacao = comparador.compare(chave, raizArvore.getChave());

    	if (comparacao < 0)
    		/// Se a chave do item a inserir for menor do que a chave do item armazenado na raiz da árvore:
    		/// insira esse item na sub-árvore esquerda e atualize a referência para a sub-árvore esquerda.
    		raizArvore.setEsquerda(inserir(raizArvore.getEsquerda(), chave, item));
    	else if (comparacao > 0)
    		/// Se a chave do item a inserir for maior do que a chave do item armazenado na raiz da árvore:
    		/// insira esse item na sub-árvore direita e atualize a referência para a sub-árvore direita.
    		raizArvore.setDireita(inserir(raizArvore.getDireita(), chave, item));
    	else
    		/// Já existe um item com essa chave na árvore.
    		throw new IllegalArgumentException("O item já foi inserido anteriormente na árvore.");

    	return raizArvore;
    }

    @Override 
    public String toString(){
    	return percorrer();
    }

    @Override
    public String percorrer() {
    	return caminhamentoEmOrdem(raiz);
    }

    private String caminhamentoEmOrdem(No<K, V> raizArvore) {
    	if (raizArvore == null)
    		return "";

    	String resposta = caminhamentoEmOrdem(raizArvore.getEsquerda());
    	resposta += raizArvore.getItem() + "\n";
    	resposta += caminhamentoEmOrdem(raizArvore.getDireita());

    	return resposta;
    }

    @Override
    /**
     * Método que encapsula a remoção recursiva de um item da árvore.
     * @param chave a chave do item que deverá ser localizado e removido da árvore.
     * @return o valor associado ao item removido.
     */
    public V remover(K chave) {
    	V removido = pesquisar(chave);

    	comparacoes = 0;
    	inicio = System.nanoTime();
    	raiz = remover(raiz, chave);
    	tamanho--;
    	termino = System.nanoTime();

    	return removido;
    }

    private No<K, V> remover(No<K, V> raizArvore, K chaveRemover) {

    	comparacoes++;
    	int comparacao = comparador.compare(chaveRemover, raizArvore.getChave());

    	if (comparacao == 0) {
    		/// O item armazenado na raiz da árvore é o item que deve ser retirado.
    		if (raizArvore.getDireita() == null)
    			/// Não há descendente à direita: a sub-árvore esquerda assume o lugar do nó removido.
    			return raizArvore.getEsquerda();
    		else if (raizArvore.getEsquerda() == null)
    			/// Não há descendente à esquerda: a sub-árvore direita assume o lugar do nó removido.
    			return raizArvore.getDireita();
    		else
    			/// O nó tem os dois descendentes: localiza-se o antecessor na sub-árvore esquerda
    			/// e ele passa a ocupar o lugar do nó removido.
    			raizArvore.setEsquerda(removerAntecessor(raizArvore, raizArvore.getEsquerda()));
    	} else if (comparacao < 0)
    		/// A chave a remover é menor: procure e remova na sub-árvore esquerda.
    		raizArvore.setEsquerda(remover(raizArvore.getEsquerda(), chaveRemover));
    	else
    		/// A chave a remover é maior: procure e remova na sub-árvore direita.
    		raizArvore.setDireita(remover(raizArvore.getDireita(), chaveRemover));

    	return raizArvore;
    }

    /**
     * Localiza, na sub-árvore informada, o antecessor do nó a ser removido (a maior chave
     * dentre as menores que a dele), transfere seus dados para o nó removido e o retira da árvore.
     * @param itemRetirar nó cujo conteúdo será substituído pelo do antecessor.
     * @param raizArvore raiz da sub-árvore em que o antecessor deve ser localizado.
     * @return a raiz atualizada da sub-árvore, após a remoção do antecessor.
     */
    private No<K, V> removerAntecessor(No<K, V> itemRetirar, No<K, V> raizArvore) {
    	if (raizArvore.getDireita() != null) {
    		/// O antecessor ainda não foi encontrado: continue procurando na sub-árvore direita.
    		raizArvore.setDireita(removerAntecessor(itemRetirar, raizArvore.getDireita()));
    		return raizArvore;
    	}

    	/// O antecessor foi encontrado: seus dados substituem os do nó a ser removido,
    	/// e sua sub-árvore esquerda assume o seu lugar.
    	itemRetirar.setChave(raizArvore.getChave());
    	itemRetirar.setItem(raizArvore.getItem());
    	return raizArvore.getEsquerda();
    }

    
    public Lista<V> recortar(K chaveDeOnde, K chaveAteOnde) {
    	Lista<V> itens = new Lista<>();
    	recortar(raiz, chaveDeOnde, chaveAteOnde, itens);
		return itens;
	}

    private void recortar(No<K, V> raizArvore, K chaveDeOnde, K chaveAteOnde, Lista<V> itens) {
    	if (raizArvore == null)
    		return;

    	int comparacaoInicio = comparador.compare(raizArvore.getChave(), chaveDeOnde);
    	int comparacaoFim = comparador.compare(raizArvore.getChave(), chaveAteOnde);

    	if (comparacaoInicio > 0)
    		/// Só há chance de existir item no intervalo à esquerda se a chave atual for maior que o início do intervalo.
    		recortar(raizArvore.getEsquerda(), chaveDeOnde, chaveAteOnde, itens);

    	if (comparacaoInicio >= 0 && comparacaoFim <= 0)
    		/// A chave do nó atual está dentro do intervalo pedido.
    		itens.inserir(raizArvore.getItem());

    	if (comparacaoFim < 0)
    		/// Só há chance de existir item no intervalo à direita se a chave atual for menor que o fim do intervalo.
    		recortar(raizArvore.getDireita(), chaveDeOnde, chaveAteOnde, itens);
    }

	@Override
	public int tamanho() {
		return tamanho;
	}
	
	@Override
	public long getComparacoes() {
		return comparacoes;
	}

	@Override
	public double getTempo() {
		return (termino - inicio) / 1_000_000;
	}
}