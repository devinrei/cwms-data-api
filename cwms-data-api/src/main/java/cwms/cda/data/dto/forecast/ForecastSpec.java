package cwms.cda.data.dto.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import cwms.cda.api.errors.FieldException;
import cwms.cda.data.dto.CwmsDTO;
import cwms.cda.data.dto.TimeSeriesIdentifierDescriptor;
import cwms.cda.formatters.Formats;
import cwms.cda.formatters.annotations.FormattableWith;
import cwms.cda.formatters.json.JsonV2;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.jetbrains.annotations.Nullable;

@XmlRootElement(name = "forecast-spec")
@XmlAccessorType(XmlAccessType.FIELD)
@FormattableWith(contentType = Formats.JSONV2, formatter = JsonV2.class)
@JsonDeserialize(builder = ForecastSpec.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class ForecastSpec extends CwmsDTO {
    @Schema(description = "Forecast Spec ID")
    @XmlElement(name = "spec-id")
    private final String specId;

    @Schema(description = "Forecast Designator")
    @XmlAttribute
    private final String designator;

    @Schema(description = "Location IDs")
    @XmlElement(name = "location-ids")
    private final Set<String> locationIds;

    @Schema(description = "Source Entity ID")
    @XmlElement(name = "source-entity-id")
    private final String sourceEntityId;

    @Schema(description = "Description of Forecast")
    @XmlAttribute
    private final String description;

    @Schema(description = "List of Time Series IDs belonging to this Forecast Spec")
    @XmlAttribute(name = "time-series-ids")
    private final List<TimeSeriesIdentifierDescriptor> timeSeriesIds;


    private ForecastSpec(Builder builder) {
        super(builder.officeId);
        this.specId = builder.specId;
        this.designator = builder.designator;

        if (builder.locationIds != null) {
            this.locationIds = new LinkedHashSet<>(builder.locationIds);
        } else {
            this.locationIds = null;
        }

        this.sourceEntityId = builder.sourceEntityId;
        this.description = builder.description;
        this.timeSeriesIds = builder.timeSeriesIds;
    }

    public String getSpecId() {
        return specId;
    }

    @Nullable
    public Set<String> getLocationIds() {
        if (locationIds == null) {
            return null;
        } else {
            return Collections.unmodifiableSet(locationIds);
        }
    }

    public String getSourceEntityId() {
        return sourceEntityId;
    }

    public String getDesignator() {
        return designator;
    }

    public String getDescription() {
        return description;
    }

    public List<TimeSeriesIdentifierDescriptor> getTimeSeriesIds() {
        return timeSeriesIds;
    }

    public void validate() throws FieldException {
        //TODO
    }

    @JsonPOJOBuilder
    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class Builder {
        private String officeId;
        private String specId;
        private String designator;
        private Set<String> locationIds;
        private String sourceEntityId;
        private String description;
        private List<TimeSeriesIdentifierDescriptor> timeSeriesIds;

        public Builder() {

        }

        public Builder withOfficeId(String officeId) {
            this.officeId = officeId;
            return this;
        }

        public Builder withSpecId(String specId) {
            this.specId = specId;
            return this;
        }

        public Builder withDesignator(String designator) {
            this.designator = designator;
            return this;
        }

        public Builder withLocationIds(Set<String> locationIds) {
            this.locationIds = locationIds;
            return this;
        }

        public Builder withSourceEntityId(String sourceEntityId) {
            this.sourceEntityId = sourceEntityId;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withTimeSeriesIds(List<TimeSeriesIdentifierDescriptor> timeSeriesIds) {
            this.timeSeriesIds = timeSeriesIds;
            return this;
        }

        public Builder from(ForecastSpec forecastSpec) {

            return withOfficeId(forecastSpec.getOfficeId())
                    .withSpecId(forecastSpec.getSpecId())
                    .withDesignator(forecastSpec.getDesignator())
                    .withLocationIds(forecastSpec.getLocationIds())
                    .withSourceEntityId(forecastSpec.getSourceEntityId())
                    .withDescription(forecastSpec.getDescription())
                    .withTimeSeriesIds(forecastSpec.getTimeSeriesIds());
        }

        public ForecastSpec build() {
            return new ForecastSpec(this);
        }
    }

}
