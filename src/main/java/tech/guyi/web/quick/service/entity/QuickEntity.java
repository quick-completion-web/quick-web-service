package tech.guyi.web.quick.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import tech.guyi.web.quick.core.controller.interfaces.entry.QuickRequestEntity;

import javax.persistence.MappedSuperclass;

@Data
@DynamicUpdate
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public abstract class QuickEntity<ID> implements QuickRequestEntity<ID> {

    private Long createTime;
    private Long updateTime;

    public abstract ID getId();
    public abstract void setId(ID id);

}
