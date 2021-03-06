package com.example.headdiary.data;

public class Drug implements Cloneable{

	private String Name;
	private String Quantity;
	private int Effect; //HDDrugEffect[effect]
	
	public Drug() {
		// TODO Auto-generated constructor stub
		Name="";
		Quantity="";
		Effect=0;
	}
	
	public void setName(String name){
		Name=name;
	}
	public String getName(){
		return Name;
	}
	
	public void setQuantity(String quantity){
		Quantity=quantity;
	}
	public String getQuantity(){
		return Quantity;
	}
	
	
	public int getEffect(){
		return Effect;
	}
	
	public void setEffect(int effect){
		Effect=effect;
	}

	@Override  
	public Object clone() {  
		Drug drug = null;  
        try{  
            drug = (Drug)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return drug;  
    }             
	 
}
