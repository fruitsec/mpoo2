package serie04;

import java.util.List;
import java.util.NavigableSet;

/**
 * Modélise un annuaire téléphonique permettant d'associer une liste de
 *  numéros de téléphone à un <code>Contact</code>.
 * Une entrée au plus par contact.
 * @inv <pre>
 *     contacts() != null
 *     forall p:Contact : contains(p) == contacts().contains(p)
 *     isEmpty() == (contacts().size() == 0)
 *     forall p:contacts() :
 *         (phoneNumbers(p) != null) && (phoneNumbers(p).size() > 0)
 *         firstPhoneNumber(p) != null
 *         firstPhoneNumber(p).equals(phoneNumbers(p).get(0))
 *         forall n:phoneNumbers(p) :
 *             phoneNumbers(p).lastIndexOf(n) == phoneNumbers(p).indexOf(n)
 * </pre>
 * @cons <pre>
 *     $DESC$ Un annuaire vide.
 *     $POST$ isEmpty() </pre>
 */
public interface PhoneBook {
    
    // REQUETES
    
    /**
     * Retourne le premier numéro d'un contact.
     * @pre <pre>
     *     (p != null) && contains(p) </pre>
     */
    String firstPhoneNumber(Contact p);
    
    /**
     * Retourne une vue non modifiable de la liste des numéros de p.
     * Cette liste est partagée avec l'annuaire de sorte que tout changement 
     *  ultérieur dans l'annuaire a une incidence observable sur cette liste.
     * @pre <pre>
     *     (p != null) && contains(p) </pre>
     */
    List<String> phoneNumbers(Contact p);
    
    /**
     * Retourne une vue ordonnée des personnes contenues dans l'annuaire.
     * Les ajouts sur cet ensemble sont impossibles.
     * Cet ensemble est partagé avec l'annuaire de sorte que le retrait d'un
     *  élément de cet ensemble entraine la suppression dans l'annuaire de
     *  l'entrée correspondante.
     * Dans l'autre sens, tout ajout ou toute suppression d'entrée dans
     *  l'annuaire entraine l'ajout ou la suppression du contact correspondant
     *  dans cet ensemble.
     */
    NavigableSet<Contact> contacts();
    
    /**
     * Retourne vrai ssi p est dans l'annuaire.
     * @pre <pre>
     *     p != null </pre>
     */
    boolean contains(Contact p);
    
    /**
     * Retourne vrai ssi l'annuaire est vide.
     */
    boolean isEmpty();

    // COMMANDES
    
    /**
     * Ajoute une nouvelle entrée dans l'annuaire.
     * L'ordre initial des numéros est préservé.
     * Dans le cas où un numéro serait présent plusieurs fois dans nums, seule
     *  la première occurrence (en partant de la gauche) sera conservée.
     * @pre <pre>
     *     (p != null) && !contains(p)
     *     (nums != null) && (nums.size() > 0) </pre>
     * @post <pre>
     *     Soit list ::= phoneNumbers(p)
     *          forall x : list_x ::= phoneNumbers(p).get(x)
     *          forall x : nums_x ::= nums.get(x)
     *     contains(p)
     *     forall n:[0..|nums| - 1], exist i:[0..|list| - 1] :
     *         i <= n && list_i.equals(nums_n)
     *         forall j:[0..|list| - 1] : j != i ==> !list_j.equals(list_i)
     *     forall m, n, i, j : list_i.equals(nums_m) && list_j.equals(nums_n)
     *         m < n ==> i <= j </pre>
     */
    void addEntry(Contact p, List<String> nums);
    
    /**
     * Ajoute un numéro à la fin de la liste des numéros d'un contact.
     * Si la personne n'existe pas, une nouvelle entrée est créée pour cette
     *  personne avec la liste constituée du numéro passé en paramètre.
     * Ne fait rien si le numéro est déjà dans la liste de p.
     * @pre <pre>
     *     p != null
     *     (n != null) && !n.equals("") </pre>
     * @post <pre>
     *     contains(p)
     *     !old contains(p)
     *         ==> phoneNumbers(p).size() == 1
     *             firstPhoneNumber(p).equals(n)
     *     old contains(p)
     *         ==> Soit oldSize ::= old phoneNumbers(p).size()
     *             !old phoneNumbers(p).contains(n)
     *                 ==> phoneNumbers(p).size() == oldSize + 1
     *                     phoneNumbers(p).indexOf(n) == oldSize </pre>
     */
    void addPhoneNumber(Contact p, String n);
    
    /**
     * Supprime un contact de l'annuaire.
     * @pre <pre>
     *     (p != null) && contains(p) </pre>
     * @post <pre>
     *     !contains(p) </pre>
     */
    void removeEntry(Contact p);
    
    /**
     * Supprime un numéro donné pour un contact donné.
     * Si ce numéro est le seul numéro de la personne, l'entrée complète est
     *  retirée de l'annuaire.
     * Si ce numéro était le premier, l'ancien second devient le nouveau
     *  premier.
     * Ne fait rien si le numéro n'est pas dans la liste des numéros de p.
     * @pre <pre>
     *     (p != null) && contains(p)
     *     (n != null) && !n.equals("") </pre>
     * @post <pre>
     *     old phoneNumbers(p).contains(n) 
     *         ==> old phoneNumbers(p).size() == 1
     *                 ==> !contains(p)
     *             old phoneNumbers(p).size() > 1
     *                 ==> Soit oldSize ::= old phoneNumbers(p).size()
     *                     Soit oldIndex ::= old  phoneNumbers(p).indexOf(n)
     *                     phoneNumbers(p).size() == oldSize - 1
     *                     forall i:[oldIndex..phoneNumbers(p).size() - 1] :
     *                         phoneNumbers(p).get(i).equals(
     *                             old phoneNumbers(p).get(i + 1)
     *                         ) </pre>
     */
    void deletePhoneNumber(Contact p, String n);
    
    /**
     * Réinitialise l'annuaire.
     * @post <pre>
     *     isEmpty() </pre>
     */
    void clear();
}
