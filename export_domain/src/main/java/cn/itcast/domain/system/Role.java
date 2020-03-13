package cn.itcast.domain.system;

import lombok.Data;

@Data
public class Role {
    /** 
     */ 
    private String id;

    /** 
     */ 
    private String name;

    /** 
     */ 
    private String remark;

    /**
     * 省略getter，setter
     */ 
    private Long orderNo;

	private String companyId;
	private String companyName;


	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}