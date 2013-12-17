package com.liminghui.godutch.domain;

public class Stock {
	private int Id;
	private String stock;
	private String vref;
	private String partno;
	private String barcode;
	private boolean acvite;
	private String serialno;
	

	private String maincate;
	private String des;
	private String shortdes;
	private String unit;
	private String category;
	private String brand;
	private String costcurr;
	private String pricecurr;
	private double cost;
	private double price;
	private double delaprice;
	private double btnprice;
	private String vendor;
	private String vendorname;
	private double cbalqty;
	private double stktake;
	private double onholdqty;
	private double rmaqty;
	private double purqty;
	private double minqty;
	private double maxqty;
	private String rem;
	private String rem2;
	private String createdate;
	private String cost_wa_dt;
	private double cost_wa;
	private double totcos_wa;
	private String picfile;
	private String power;
	private String opowerrms;
	private String opowerpmpo;
	private String gbbarcode;
	private String ocbarcode;
	private String pbarcode;
	private String sclass;
	private String subclass;
	private int edittime;
	private String firstmdate;
	private String fusername;
	private String lastmdate;
	private String username;
	private String client;
	private String address;
	private String deliveryAddress;

	public Stock() {

	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	
	public String getVref() {
		return vref;
	}

	public void setVref(String vref) {
		this.vref = vref;
	}
	
	public String getPartno() {
		return partno;
	}

	public void setPartno(String partno) {
		this.partno = partno;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public boolean isAcvite() {
		return acvite;
	}

	public void setAcvite(boolean acvite) {
		this.acvite = acvite;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getMaincate() {
		return maincate;
	}

	public void setMaincate(String maincate) {
		this.maincate = maincate;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getShortdes() {
		return shortdes;
	}

	public void setShortdes(String shortdes) {
		this.shortdes = shortdes;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCostcurr() {
		return costcurr;
	}

	public void setCostcurr(String costcurr) {
		this.costcurr = costcurr;
	}

	public String getPricecurr() {
		return pricecurr;
	}

	public void setPricecurr(String pricecurr) {
		this.pricecurr = pricecurr;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDelaprice() {
		return delaprice;
	}

	public void setDelaprice(double delaprice) {
		this.delaprice = delaprice;
	}

	public double getBtnprice() {
		return btnprice;
	}

	public void setBtnprice(double btnprice) {
		this.btnprice = btnprice;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public double getCbalqty() {
		return cbalqty;
	}

	public void setCbalqty(double cbalqty) {
		this.cbalqty = cbalqty;
	}

	public double getStktake() {
		return stktake;
	}

	public void setStktake(double stktake) {
		this.stktake = stktake;
	}

	public double getOnholdqty() {
		return onholdqty;
	}

	public void setOnholdqty(double onholdqty) {
		this.onholdqty = onholdqty;
	}

	public double getRmaqty() {
		return rmaqty;
	}

	public void setRmaqty(double rmaqty) {
		this.rmaqty = rmaqty;
	}

	public double getPurqty() {
		return purqty;
	}

	public void setPurqty(double purqty) {
		this.purqty = purqty;
	}

	public double getMinqty() {
		return minqty;
	}

	public void setMinqty(double minqty) {
		this.minqty = minqty;
	}

	public double getMaxqty() {
		return maxqty;
	}

	public void setMaxqty(double maxqty) {
		this.maxqty = maxqty;
	}

	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getRem2() {
		return rem2;
	}

	public void setRem2(String rem2) {
		this.rem2 = rem2;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getCost_wa_dt() {
		return cost_wa_dt;
	}

	public void setCost_wa_dt(String cost_wa_dt) {
		this.cost_wa_dt = cost_wa_dt;
	}

	public double getCost_wa() {
		return cost_wa;
	}

	public void setCost_wa(double cost_wa) {
		this.cost_wa = cost_wa;
	}

	public double getTotcos_wa() {
		return totcos_wa;
	}

	public void setTotcos_wa(double totcos_wa) {
		this.totcos_wa = totcos_wa;
	}

	public String getPicfile() {
		return picfile;
	}

	public void setPicfile(String picfile) {
		this.picfile = picfile;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public String getOpowerrms() {
		return opowerrms;
	}

	public void setOpowerrms(String opowerrms) {
		this.opowerrms = opowerrms;
	}

	public String getOpowerpmpo() {
		return opowerpmpo;
	}

	public void setOpowerpmpo(String opowerpmpo) {
		this.opowerpmpo = opowerpmpo;
	}

	public String getGbbarcode() {
		return gbbarcode;
	}

	public void setGbbarcode(String gbbarcode) {
		this.gbbarcode = gbbarcode;
	}

	public String getOcbarcode() {
		return ocbarcode;
	}

	public void setOcbarcode(String ocbarcode) {
		this.ocbarcode = ocbarcode;
	}

	public String getPbarcode() {
		return pbarcode;
	}

	public void setPbarcode(String pbarcode) {
		this.pbarcode = pbarcode;
	}

	public String getSclass() {
		return sclass;
	}

	public void setSclass(String sclass) {
		this.sclass = sclass;
	}

	public String getSubclass() {
		return subclass;
	}

	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}

	public int getEdittime() {
		return edittime;
	}

	public void setEdittime(int edittime) {
		this.edittime = edittime;
	}

	public String getFirstmdate() {
		return firstmdate;
	}

	public void setFirstmdate(String firstmdate) {
		this.firstmdate = firstmdate;
	}

	public String getFusername() {
		return fusername;
	}

	public void setFusername(String fusername) {
		this.fusername = fusername;
	}

	public String getLastmdate() {
		return lastmdate;
	}

	public void setLastmdate(String lastmdate) {
		this.lastmdate = lastmdate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

}
