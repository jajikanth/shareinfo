package com.jitworks.shareinfo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "shareinfo_role_type")
public class Role implements GrantedAuthority {
	private static final long serialVersionUID = -1133653735146438988L;

	@Id
	@Column(name = "role_code")
	private String code;

	@Column(name = "role_name")
	private String name;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthority() {
		return this.code;
	}
}
