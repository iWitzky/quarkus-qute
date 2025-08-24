package org.acme.todo;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.hibernate.annotations.TenantId;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "TODO")
@SequenceGenerator(name = "todo_id_seq", sequenceName = "todo_id_seq")
public class Todo extends PanacheEntity {

    @Column(nullable = false, length = 200)
    public String title;
    public int priority;
    public boolean completed;

    @SuppressWarnings("unused")
    @TenantId
    public String tenantId;
}
