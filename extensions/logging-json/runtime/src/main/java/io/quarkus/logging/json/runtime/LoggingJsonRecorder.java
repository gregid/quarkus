package io.quarkus.logging.json.runtime;

import java.util.Optional;
import java.util.logging.Formatter;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class LoggingJsonRecorder {
    public RuntimeValue<Optional<Formatter>> initializeJsonLogging(final JsonConfig config) {
        if (!config.enable) {
            return new RuntimeValue<>(Optional.empty());
        }
        final JsonFormatter formatter = config.keyOverrides.map(ko -> new JsonFormatter(ko)).orElse(new JsonFormatter());
        config.excludedKeys.ifPresent(ek -> formatter.setExcludedKeys(ek));
        Optional.ofNullable(config.additionalField).ifPresent(af -> formatter.setAdditionalFields(af));
        formatter.setPrettyPrint(config.prettyPrint);
        final String dateFormat = config.dateFormat;
        if (!dateFormat.equals("default")) {
            formatter.setDateFormat(dateFormat);
        }
        formatter.setExceptionOutputType(config.exceptionOutputType);
        formatter.setPrintDetails(config.printDetails);
        config.recordDelimiter.ifPresent(formatter::setRecordDelimiter);
        final String zoneId = config.zoneId;
        if (!zoneId.equals("default")) {
            formatter.setZoneId(zoneId);
        }
        return new RuntimeValue<>(Optional.of(formatter));
    }
}
