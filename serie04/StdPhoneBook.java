package serie04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;

import util.Contract;

public class StdPhoneBook implements PhoneBook {

	private Map<Contact, List<String>> annuaire;
	
	public StdPhoneBook() {
		this.annuaire = new TreeMap<Contact,List<String>>();
	}
	
	/**
     * Retourne le premier numéro d'un contact.
     * @pre <pre>
     *     (p != null) && contains(p) </pre>
     */
	@Override
	public String firstPhoneNumber(Contact p) {
		Contract.checkCondition(p != null, "Le contact ne peut pas être nul.");
		Contract.checkCondition(contains(p), "Le contact n'est pas dans l'annuaire.");
		
		return annuaire.get(p).get(0);
	}

	/**
     * Retourne une vue non modifiable de la liste des numéros de p.
     * Cette liste est partagée avec l'annuaire de sorte que tout changement 
     *  ultérieur dans l'annuaire a une incidence observable sur cette liste.
     * @pre <pre>
     *     (p != null) && contains(p) </pre>
     */
	@Override
	public List<String> phoneNumbers(Contact p) {
		Contract.checkCondition(p != null && contains(p), "Le contact est introuvable.");
		
		List<String> numsTel = annuaire.get(p);
		numsTel = Collections.unmodifiableList(numsTel);
		
		return numsTel;
	}

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
	@Override
	public NavigableSet<Contact> contacts() {
		NavigableSet<Contact> ns = (NavigableSet<Contact>) annuaire.keySet();
		Collections.unmodifiableSet(ns);
		return ns;
	}

	/**
     * Retourne vrai ssi p est dans l'annuaire.
     * @pre <pre>
     *     p != null </pre>
     */
	@Override
	public boolean contains(Contact p) {
		Contract.checkCondition(p != null, "Le contact ne peut pas être null.");
		return annuaire.containsKey(p);
	}

	/**
     * Retourne vrai ssi l'annuaire est vide.
     */
	@Override
	public boolean isEmpty() {
		return annuaire.size() == 0;
	}

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
	@Override
	public void addEntry(Contact p, List<String> nums) {
		Contract.checkCondition(p != null && !contains(p), "Impossible d'ajouter ce contact.");
		Contract.checkCondition(nums != null && nums.size() > 0, "Impossible d'ajouter ce(s) numéro(s).");
		
		List<String> numFinal = new ArrayList<String>();
		
		for(String s : nums) {
			if(!numFinal.contains(s)) {
				numFinal.add(s);
			}
		}
		
		annuaire.put(p, numFinal);
	}

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
	@Override
	public void addPhoneNumber(Contact p, String n) {
		Contract.checkCondition(p != null, "Le contact est introuvable.");
		Contract.checkCondition(n != null && !n.equals(""), "Le numéro n'est pas valide.");
		
		if(contains(p)) {
			ArrayList<String> verif = (ArrayList<String>) annuaire.get(p);
			if(!verif.contains(n)) {
				verif.add(n);
				annuaire.put(p, verif);
			}
		}
		else {
			ArrayList<String> tmp = new ArrayList<String>();
			tmp.add(n);
			annuaire.put(p, tmp);
		}

	}

	/**
     * Supprime un contact de l'annuaire.
     * @pre <pre>
     *     (p != null) && contains(p) </pre>
     * @post <pre>
     *     !contains(p) </pre>
     */
	@Override
	public void removeEntry(Contact p) {
		Contract.checkCondition(p != null && contains(p), "Le contact est introuvable.");
		
		annuaire.remove(p);
	}

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
	@Override
	public void deletePhoneNumber(Contact p, String n) {
		Contract.checkCondition(p != null && contains(p), "Le contact est introuvable.");
		Contract.checkCondition(n != null && !n.equals(""), "Le numéro n'est pas valide.");
		
		ArrayList<String> tmp = (ArrayList<String>) annuaire.get(p);
		
		if(tmp.size() == 1) {
			removeEntry(p);
		}
		else if(tmp.get(0).equals(n)) {
			tmp.remove(n);
			annuaire.put(p, tmp);
		}
	}

	/**
     * Réinitialise l'annuaire.
     * @post <pre>
     *     isEmpty() </pre>
     */
	@Override
	public void clear() {
		annuaire.clear();
	}

}
