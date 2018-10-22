package serie05;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import serie04.Civ;
import serie04.PhoneBook;

/**
 * Modélise un annuaire téléphonique permettant de sauvegarder dans et
 *  charger à partir d'un fichier de texte.
 * Les fichiers textes associés aux annuaires doivent impérativement
 *  respecter le format suivant :
 * <ul>
 *    <li>Une entrée par ligne</li>
 *    <li>Une ligne est constituée, dans cet ordre,
 *       <ol>
 *          <li>d'un entier entre 0 et le nombre de civilités moins une
 *              suivi du caractère ':'</li>
 *          <li>d'un nom suivi du caractère ':'</li>
 *          <li>d'un prénom suivi du caractère ':'</li>
 *          <li>d'un numéro de téléphone</li>
 *          <li>[d'une virgule suivie d'un numéro de téléphone]*</li>
 *       </ol>
 *    </li>
 * </ul>
 * @cons
 * $DESC$
 *     Un annuaire éventuellement associé à un fichier de chemin 'file'
 * $ARGS$
 *     File file
 * $POST$
 *     getFile() == file
 * @cons
 * $DESC$
 *     Un annuaire sans fichier associé
 * $ARGS$ -
 * $POST$
 *     getFile() == null
 */

public interface PersistentPhoneBook extends PhoneBook {
    
    // CONSTANTES
    
    // Le pattern qui permet de tester la validité d'une ligne du fichier :
    //  des blancs (0, 1 ou plusieurs)
    //  un chiffre représentant la civilité (0..3)
    //  des blancs (0, 1 ou plusieurs)
    //  :
    //  une chaîne (qui ne contient pas ":")
    //  :
    //  une chaîne (qui ne contient pas ":")
    //  :
    //  des blancs (0, 1 ou plusieurs)
    //  un no de téléphone (dd.dd.dd.dd.dd où d représente un chiffre)
    //  des blancs (0, 1 ou plusieurs)
    //  {
    //    une virgule (",")
    //    des blancs (0, 1 ou plusieurs)
    //    un numéro de téléphone
    //    des blancs (0, 1 ou plusieurs)
    //  } 0, 1 ou plusieurs fois
    Pattern LINE_RECOGNIZER = Pattern.compile(
            "^\\s*[0-" + (Civ.values().length - 1) // civ
            + "]\\s*:[^:]+:[^:]+:"                 // :nom:prénom:
            + "\\s*(\\d\\d\\.){4}\\d\\d\\s*"       // dd.dd.dd.dd.dd
            + "(,\\s*(\\d\\d\\.){4}\\d\\d\\s*)*$"  // , no, no, ...
    );

    // REQUETES
    
    /**
     * Retourne le chemin du fichier de sauvegarde associé à cet annuaire.
     * Retourne null s'il n'y a pas de fichier associé.
     */
    File getFile();
    
    // COMMANDES
    
    /**
     * Change de chemin de fichier de sauvegarde associé à cet annuaire.
     * @pre
     *     file != null
     * @post
     *     getFile() == file
     */
    void setFile(File file);
    
    /**
     * Remplace le contenu de cet annuaire par celui du fichier texte.
     * Si le chargement est impossible, une BadSyntaxException est levée et
     *  l'annuaire est alors vide.
     * @pre
     *     getFile() != null
     * @post
     *     L'annuaire a été réinitialisé avec le contenu du fichier associé
     *      si tout s'est bien passé
     *     L'annuaire est vide si une exception a été levée
     * @throws BadSyntaxException
     *     si le fichier associé est mal formé
     * @throws java.io.FileNotFoundException
     *     si le fichier associé n'existe pas ou n'est pas accessible en lecture
     * @throws IOException
     *     s'il se produit une erreur d'entrée/sortie lors de la lecture
     *      du fichier associé
     */
    void load() throws IOException, BadSyntaxException;
    
    /**
     * Sauvegarde le contenu de l'annuaire dans son fichier associé.
     * @pre
     *     getFile() != null
     * @post
     *     Le fichier associé est un fichier de texte dont le contenu
     *      (correctement formé) est celui de l'annuaire si tout
     *      s'est bien passé.
     *     Il ne doit y avoir aucun blanc superflu.
     * @throws java.io.FileNotFoundException
     *     si le fichier associé ne peut pas être créé ou n'est pas accessible
     *      en écriture
     * @throws IOException
     *     s'il se produit une erreur d'entrée/sortie lors de l'écriture
     *      dans le fichier associé
     */
    void save() throws IOException;
}
