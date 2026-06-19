package main.business.dto;

import java.sql.Date;

public class PeriodoDTO {

    private final int periodId;
    private final String name;
    private final Date startDate;
    private final Date endDate;
    private final int coordinatorId;
    private final String coordinatorName;

    private PeriodoDTO(PeriodoBuilder builder) {
        this.periodId = builder.periodId;
        this.name = builder.name;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.coordinatorId = builder.coordinatorId;
        this.coordinatorName = builder.coordinatorName;
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

    public int getCoordinatorId() {
        return coordinatorId;
    }

    public String getCoordinatorName() {
        return coordinatorName;
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
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }
        PeriodoDTO that = (PeriodoDTO) instance;
        return periodId == that.periodId
                && name.equals(that.name);
    }

    @Override
    public String toString() {
        return name;
    }

    public static class PeriodoBuilder {

        private int periodId;
        private String name;
        private Date startDate;
        private Date endDate;
        private int coordinatorId;
        private String coordinatorName;

        public PeriodoBuilder setPeriodId(int periodId) {
            this.periodId = periodId;
            return this;
        }

        public PeriodoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PeriodoBuilder setStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public PeriodoBuilder setEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public PeriodoBuilder setCoordinatorId(int coordinatorId) {
            this.coordinatorId = coordinatorId;
            return this;
        }

        public PeriodoBuilder setCoordinatorName(String coordinatorName) {
            this.coordinatorName = coordinatorName;
            return this;
        }

        public PeriodoDTO build() {
            return new PeriodoDTO(this);
        }
    }
}
