package main.business.dto;

import java.sql.Date;

public class PeriodDTO {

    private final int periodId;
    private final String name;
    private final Date startDate;
    private final Date endDate;

    private PeriodDTO(PeriodBuilder builder) {
        this.periodId = builder.periodId;
        this.name = builder.name;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
    }

    public int getPeriodId() {
        return periodId;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStatus() {
        if (startDate == null || endDate == null) return "Desconocido";
        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate inicio = startDate.toLocalDate();
        java.time.LocalDate fin = endDate.toLocalDate();
        if (now.isBefore(inicio)) return "Próximo";
        if (now.isAfter(fin)) return "Concluido";
        return "Activo";
    }

    @Override
    public String toString() {
        return name;
    }

    public static class PeriodBuilder {

        private int periodId;
        private String name;
        private Date startDate;
        private Date endDate;

        public PeriodBuilder setPeriodId(int periodId) {
            this.periodId = periodId;
            return this;
        }

        public PeriodBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PeriodBuilder setStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public PeriodBuilder setEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public PeriodDTO build() {
            return new PeriodDTO(this);
        }
    }
}
