package com.sento.organisations.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "organisations")
public class Organisation {
    @Override
    public String toString() {
        return "Organisation [id=" + id + ", orgId=" + orgId + ", name=" + name + ", contactName=" + contactName
                + ", contactEmail=" + contactEmail + ", contactPhone=" + contactPhone + "]";
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Organisation other = (Organisation) obj;
        if (orgId == null) {
            if (other.orgId != null)
                return false;
        } else if (!orgId.equals(other.orgId))
            return false;
        return true;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "org_id", nullable = false, unique = true)
    private String orgId;

    public String getOrgId() {
        return orgId;
    }


    public void setOrgId(String org_id) {
        this.orgId = org_id;
    }

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "contact_name", nullable = false)
    private String contactName;

    @NotNull
    @Email
    @Column(name = "email", nullable = false)
    private String contactEmail;

    @Column(name = "contact_phone", nullable = false)
    private  String contactPhone;

    @JsonIgnore
    public int getId() {
        return id;
    }

    @JsonIgnore
    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

}

