package cwms.cda.data.dto;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import cwms.cda.api.enums.VersionType;
import cwms.cda.api.errors.FieldException;
import cwms.cda.data.dto.TimeSeries.Record;
import cwms.cda.formatters.Formats;
import cwms.cda.formatters.annotations.FormattableWith;
import cwms.cda.formatters.json.JsonV2;
import cwms.cda.formatters.xml.XMLv2;
import cwms.cda.formatters.xml.adapters.DurationAdapter;
import cwms.cda.formatters.xml.adapters.TimestampAdapter;
import cwms.cda.formatters.xml.adapters.ZonedDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;

@XmlRootElement(name="timeseries")
@XmlSeeAlso(Record.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@JsonPropertyOrder(alphabetic = true)
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
@FormattableWith(contentType = Formats.JSONV2, formatter = JsonV2.class)
@FormattableWith(contentType = Formats.XMLV2, formatter = XMLv2.class)
public class TimeSeries extends CwmsDTOPaginated {
    public static final String ZONED_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ'['VV']'";

    @Schema(description = "Time-series name")
    String name;

    @Schema(description = "Office ID that owns the time-series")
    @XmlElement(name = "office-id")
    String officeId;

    @Schema(description = "The units of the time series data",required = true)
    String units;

    @Schema(description = "The version type for the time series being queried. Can be in the form of MAX_AGGREGATE, SINGLE_VERSION, or UNVERSIONED. " +
            "MAX_AGGREGATE will get the latest version date value for each value in the date range. SINGLE_VERSION must be called with a valid " +
            "version date and will return the values for the version date provided. UNVERSIONED return values from an unversioned time series. " +
            "Note that SINGLE_VERSION requires a valid version date while MAX_AGGREGATE and UNVERSIONED each require a null version date.")
    @JsonFormat(shape = Shape.STRING)
    VersionType dateVersionType;

    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    @JsonFormat(shape = Shape.STRING)
    @Schema(description = "The version date of the time series trace")
    ZonedDateTime versionDate;

    @XmlJavaTypeAdapter(DurationAdapter.class)
    @JsonFormat(shape = Shape.STRING)
    @Schema(
            accessMode = AccessMode.READ_ONLY,
            format = "Java Duration",
            description = "The interval of the time-series, in ISO-8601 duration format"
    )
    Duration interval;

    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    @JsonFormat(shape = Shape.STRING)
    @Schema(
            accessMode = AccessMode.READ_ONLY,
            description = "The requested start time of the data, in ISO-8601 format with offset and timezone ('" + ZONED_DATE_TIME_FORMAT + "')"
    )
    ZonedDateTime begin;

    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    @JsonFormat(shape = Shape.STRING)
    @Schema(
            accessMode = AccessMode.READ_ONLY,
            description = "The requested end time of the data, in ISO-8601 format with offset and timezone ('" + ZONED_DATE_TIME_FORMAT + "')"
    )
    ZonedDateTime end;

    @XmlElementWrapper
    @XmlElement(name="record")
    // Use the array shape to optimize data transfer to client
    @JsonFormat(shape=JsonFormat.Shape.ARRAY)
    @ArraySchema(
            schema = @Schema(
                    description = "List of retrieved time-series values",
                    implementation = Record.class
            )
    )
    List<Record> values;

    @Schema(
            accessMode = AccessMode.READ_ONLY,
            description = "Information about where the measurement takes place in relation to a defined dataum."
    )
    VerticalDatumInfo verticalDatumInfo;

    @Schema(
            accessMode = AccessMode.READ_ONLY,
            description="Offset from top of interval"
    )
    @XmlElement(name="interval-offset")
    private Long intervalOffset;

    @Schema(
            accessMode = AccessMode.READ_ONLY,
            description = "Only on 21.1.1 Database. The timezone the Interval Offset is from."
    )
    @XmlElement(name="time-zone")
    private String timeZone;


    @SuppressWarnings("unused") // required so JAXB can initialize and marshal
    private TimeSeries() {}

    public TimeSeries(String page, int pageSize, Integer total, String name, String officeId, ZonedDateTime begin, ZonedDateTime end, String units, Duration interval) {
        this(page, pageSize, total, name, officeId, begin, end, units, interval, null, null, null, null, null);
    }

    public TimeSeries(String page, int pageSize, Integer total, String name, String officeId, ZonedDateTime begin, ZonedDateTime end, String units, Duration interval, VerticalDatumInfo info, ZonedDateTime versionDate, VersionType dateVersionType){
        this(page, pageSize, total, name, officeId, begin, end,  units, interval, info, null, null, versionDate, dateVersionType);
    }

    public TimeSeries(String page, int pageSize, Integer total, String name, String officeId, ZonedDateTime begin, ZonedDateTime end, String units, Duration interval, VerticalDatumInfo info, Long intervalOffset, String timeZone, ZonedDateTime versionDate, VersionType dateVersionType) {
        super(page, pageSize, total);
        this.name = name;
        this.officeId = officeId;
        this.begin = begin;
        this.end = end;
        this.versionDate = versionDate;
        this.dateVersionType = dateVersionType;
        this.interval = interval;
        this.units = units;
        this.verticalDatumInfo = info;
        this.intervalOffset = intervalOffset;
        this.timeZone = timeZone;
        values = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getOfficeId() {
        return officeId;
    }

    public String getUnits() {
        return units;
    }

    @XmlTransient
    @JsonIgnore
    public long getIntervalMinutes() {
        return interval.toMinutes();
    }

    public Duration getInterval() {
        return interval;
    }

    public ZonedDateTime getBegin() {
        return begin;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public List<Record> getValues() {
        return values;
    }

    public VerticalDatumInfo getVerticalDatumInfo()
    {
        return verticalDatumInfo;
    }

    public Long getIntervalOffset() {
        return intervalOffset;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public ZonedDateTime getVersionDate() {
        return versionDate;
    }

    public VersionType getDateVersionType() { return dateVersionType; }

    @XmlElementWrapper(name="value-columns")
    @XmlElement(name="column")
    @JsonIgnore
    public List<Column> getValueColumnsXML() {
        return getColumnDescriptor("xml");
    }

    @XmlTransient
    @JsonProperty(value = "value-columns")
    @Schema(name = "value-columns", accessMode = AccessMode.READ_ONLY)
    public List<Column> getValueColumnsJSON() {
        return getColumnDescriptor("json");
    }

    public boolean addValue(Timestamp dateTime, Double value, int qualityCode) {
        // Set the current page, if not set
        if((page == null || page.isEmpty()) && values.isEmpty()) {
            page = encodeCursor(String.format("%d", dateTime.getTime()), pageSize, total);
        }
        if(pageSize > 0 && values.size() == pageSize) {
            nextPage = encodeCursor(String.format("%d", dateTime.toInstant().toEpochMilli()), pageSize, total);
            return false;
        } else {
            return values.add(new Record(dateTime, value, qualityCode));
        }
    }

    private List<Column> getColumnDescriptor(String format) {
        List<Column> columns = new ArrayList<>();

        for (Field f: Record.class.getDeclaredFields()) {
            String fieldName = f.getName();
            int fieldIndex = -1;
            if(format.equals("json")) {
                JsonProperty field = f.getAnnotation(JsonProperty.class);
                if(field != null)
                    fieldName = !field.value().isEmpty() ? field.value() : f.getName();
                fieldIndex = field.index();
            }
            else if(format.equals("xml")) {
                XmlType xmltype = Record.class.getAnnotation(XmlType.class);
                if(xmltype != null) {
                    String[] props = xmltype.propOrder();
                    for(int idx = 0; idx < props.length; idx++) {
                        if( props[idx].equals(fieldName)) {
                            fieldIndex = idx;
                            break;
                        }
                    }
                }
            }
            else {
                fieldIndex++;
            }

            columns.add(new TimeSeries.Column(fieldName, fieldIndex + 1, f.getType()));
        }

        return columns;
    }



    @ArraySchema(
            schema = @Schema(
                    name = "TimeSeries.Record",
                    description = "A representation of a time-series record in the form [dateTime, value, qualityCode]",
                    type="array"
            ),
            arraySchema = @Schema(
                    type="array",
                    example = "[1509654000000, 54.3, 0]"
            )
    )
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement
    @XmlType(propOrder = {"dateTime", "value", "qualityCode"})
    public static class Record {
        // Explicitly set property order for array serialization
        @JsonProperty(value = "date-time", index = 0)
        @XmlJavaTypeAdapter(TimestampAdapter.class)
        @XmlElement(name = "date-time")
        @Schema(implementation = Long.class, description = "Milliseconds since 1970-01-01 (Unix Epoch), always UTC")
        Timestamp dateTime;

        @JsonProperty(index = 1)
        @Schema(description = "Requested time-series data value")
        Double value;

        @XmlElement(name = "quality-code")
        @JsonProperty(value = "quality-code", index = 2)
        int qualityCode;

        @SuppressWarnings("unused") // required so JAXB can initialize and marshal
        private Record() {}

        protected Record(Timestamp dateTime, Double value, int qualityCode) {
            this.dateTime = dateTime;
            this.value = value;
            this.qualityCode = qualityCode;
        }

        // When serialized, the value is unix epoch at UTC.
        public Timestamp getDateTime() {
            return dateTime;
        }

        public Double getValue() {
            return value;
        }

        public int getQualityCode() {
            return qualityCode;
        }

        @Override
        public boolean equals(Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null || getClass() != o.getClass())
            {
                return false;
            }

            final Record record = (Record) o;

            if(getQualityCode() != record.getQualityCode())
            {
                return false;
            }
            if(getDateTime() != null ? !getDateTime().equals(record.getDateTime()) : record.getDateTime() != null)
            {
                return false;
            }
            return getValue() != null ? getValue().equals(record.getValue()) : record.getValue() == null;
        }

        @Override
        public int hashCode()
        {
            int result = getDateTime() != null ? getDateTime().hashCode() : 0;
            result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
            result = 31 * result + getQualityCode();
            return result;
        }

        @Override
        public String toString()
        {
            return "Record{" + "dateTime=" + dateTime + ", value=" + value + ", qualityCode=" + qualityCode + '}';
        }
    }

    @Schema(hidden = true, name = "TimeSeries.Column", accessMode = Schema.AccessMode.READ_ONLY)
    private static class Column {
        public final String name;
        public final int ordinal;
        public final Class<?> datatype;

        // JAXB seems to need a default ctor
        private Column(){
            this(null, 0,null);
        }

        @JsonCreator
        protected Column(@JsonProperty("name") String name, @JsonProperty("ordinal") int number, @JsonProperty("datatype") Class<?> datatype) {
            this.name = name;
            this.ordinal = number;
            this.datatype = datatype;
        }

        public String getDatatype() {
            return datatype.getTypeName();
        }
    }

    @Override
    public void validate() throws FieldException {
        // TODO Auto-generated method stub

    }
}
