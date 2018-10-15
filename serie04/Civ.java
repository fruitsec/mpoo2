package serie04;

import util.Contract;

public enum Civ {
	UKN(""),
	MR("M."),
	MRS("Mme"),
	MS("Mlle");
	
	private final String civilite;
	
	private Civ(String civilite) {
		this.civilite = civilite;
	}
	
	/**
	  * @pre
	  *     candidate != null
	  * @post
	  *     this == UKN ==> result == { MR, MRS, MS }.contains(candidate)
	  *     this == MR  ==> result == false
	  *     this == MRS ==> result == (candidate == MS)
	  *     this == MS  ==> result == (candidate == MRS)
	  */
	 public boolean canEvolveTo(Civ candidate) {
		 Contract.checkCondition(candidate != null, "La civilité ne peut pas être nulle.");
		 
		 switch(this) {
			 case UKN:
				 if(candidate.name().equals("UKN")) {
					 return false;
				 }
				 else {
					 return true;
				 }
			 case MR:
				 return false;
			 case MRS:
				 if(candidate.name().equals("MS")) {
					 return true;
				 }
				 break;
			 case MS:
				 if(candidate.name().equals("MRS")) {
					 return true;
				 }
				 break;		 
		 }
		 return false;
	 };
	 
	 /**
	  * @post
	  *     this == UKN ==> result.equals("")
	  *     this == MR  ==> result.equals("M.")
	  *     this == MRS ==> result.equals("Mme")
	  *     this == MS  ==> result.equals("Mlle")
	  */
	 public String toString() {
		 return civilite;
	 }
}
