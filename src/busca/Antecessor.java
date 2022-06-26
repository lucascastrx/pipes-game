package busca;

/**
 * Interface para estados que tem a função antecessores.
 *
 * @author  jomi
 * 
 */
public interface Antecessor {
    public <E extends Estado> java.util.List<E> antecessores();
}
