package serie05;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.regex.Matcher;

import serie04.Civ;
import serie04.Contact;
import serie04.StdContact;
import serie04.StdPhoneBook;
import util.Contract;

public class StdPersistentPhoneBook extends StdPhoneBook implements PersistentPhoneBook {

	private Map<Contact, List<String>> annuaire;
	
	private File fichier;

	public StdPersistentPhoneBook(File file) {
        this.annuaire = new TreeMap<Contact,List<String>>();
        this.fichier = file;
    }
	
	public StdPersistentPhoneBook() {
        this.annuaire = new TreeMap<Contact,List<String>>();
        this.fichier = null;        
    }
	
    /**
     * Retourne le chemin du fichier de sauvegarde associ� � cet annuaire.
     * Retourne null s'il n'y a pas de fichier associ�.
     */
	@Override
	public File getFile() {
		return fichier;
	}

	/**
     * Change de chemin de fichier de sauvegarde associ� � cet annuaire.
     * @pre
     *     file != null
     * @post
     *     getFile() == file
     */
	@Override
	public void setFile(File file) {
		Contract.checkCondition(file != null, "Le fichier est nul.");
		fichier = file;
	}

	/**
     * Remplace le contenu de cet annuaire par celui du fichier texte.
     * Si le chargement est impossible, une BadSyntaxException est lev�e et
     *  l'annuaire est alors vide.
     * @pre
     *     getFile() != null
     * @post
     *     L'annuaire a �t� r�initialis� avec le contenu du fichier associ�
     *      si tout s'est bien pass�
     *     L'annuaire est vide si une exception a �t� lev�e
     * @throws BadSyntaxException
     *     si le fichier associ� est mal form�
     * @throws java.io.FileNotFoundException
     *     si le fichier associ� n'existe pas ou n'est pas accessible en lecture
     * @throws IOException
     *     s'il se produit une erreur d'entr�e/sortie lors de la lecture
     *      du fichier associ�
     */
	@Override
	public void load() throws IOException, BadSyntaxException {
		Contract.checkCondition(getFile()!=null, "Le fichier est nul.");
		
		String ligne = "";
		Matcher m = LINE_RECOGNIZER.matcher(ligne);
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fichier));
			while((ligne = in.readLine()) != null) {
				if(m.reset(ligne).matches()) {
					String[] informations = ligne.split(":");
					Civ civilite = Civ.valueOf(informations[0]);
					String nom = informations[1];
					String prenom = informations[2];
					
					Contact c = new StdContact(civilite, nom, prenom);
					
					String[] numeros = informations[3].split(",");
					ArrayList<String> als = new ArrayList<String>();
					for(String s : numeros) {
						als.add(s);
					}
					
					addEntry(c,als);
				}
				else {
					clear();
					throw new BadSyntaxException();
				}
			}
		}
		catch(IOException ioe) {
			clear();
			throw ioe;
		}
		finally {
			in.close();
		}

	}

	/**
     * Sauvegarde le contenu de l'annuaire dans son fichier associ�.
     * @pre
     *     getFile() != null
     * @post
     *     Le fichier associ� est un fichier de texte dont le contenu
     *      (correctement form�) est celui de l'annuaire si tout
     *      s'est bien pass�.
     *     Il ne doit y avoir aucun blanc superflu.
     * @throws java.io.FileNotFoundException
     *     si le fichier associ� ne peut pas �tre cr�� ou n'est pas accessible
     *      en �criture
     * @throws IOException
     *     s'il se produit une erreur d'entr�e/sortie lors de l'�criture
     *      dans le fichier associ�
     */
	@Override
	public void save() throws IOException {
		Contract.checkCondition(getFile()!=null, "Le fichier est nul.");
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(fichier)));
			String resultat;
			NavigableSet<Contact> contacts = contacts();
			for(Contact c : contacts) {
				resultat = "" + c.getCivility().ordinal() + ":"
						+ c.getLastName() + ":" + c.getFirstName() + ":";
				List<String> numeros = annuaire.get(c);
				for(String s : numeros) {
					s = s.trim();
					for(int i = 0; i<8; i+=2) {
						resultat += s.substring(i,i+2) + ".";
					}
					resultat += ",";
				}
				resultat = resultat.substring(0,resultat.length()-1);
				out.write(resultat);
			}
		}
		catch(IOException ioe) {
			throw ioe;
		}
		finally {
			out.close();
		}
	}

}
