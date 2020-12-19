package tech.guyi.web.quick.service.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@DynamicUpdate
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class QuickLongEntity extends QuickEntity<Long> {

    @Id
    private Long id;

}
