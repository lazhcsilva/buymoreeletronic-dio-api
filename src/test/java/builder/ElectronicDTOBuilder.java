package builder;

import com.dititalinnovation.buymore.dto.ElectronicDTO;
import com.dititalinnovation.buymore.enums.ElectronicType;
import lombok.Builder;

@Builder
public class ElectronicDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Tv Samsung";

    @Builder.Default
    private String brand = "Samsung";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private ElectronicType type = ElectronicType.TV;

    public ElectronicDTO toElectronicDTO(){
        return new ElectronicDTO(id,
                name,
                brand,
                max,
                quantity,
                type);
    }

}
