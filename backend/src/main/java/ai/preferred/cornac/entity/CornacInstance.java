package ai.preferred.cornac.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cornac_instance")
public class CornacInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String serviceName;
    private String modelClass;
    private int port;
    @Transient
    private Process process;
    @Transient
    private WebClient webClient;
    private boolean isStopped = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id")
    private Experiment experiment;

    public CornacInstance(String serviceName, String modelClass, int port, Experiment experiment, Process process, WebClient webClient) {
        this.serviceName = serviceName;
        this.modelClass = modelClass;
        this.port = port;
        this.experiment = experiment;
        this.process = process;
        this.webClient = webClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CornacInstance that = (CornacInstance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
