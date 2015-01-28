package pack.tomainventario.tomadeinventario.Objects;


    public class ReportesContent {
        private Integer numero;
        private String descripcion;
        private String fecha;

        public ReportesContent(Integer numero, String descripcion, String fecha){
            this.numero = numero;
            this.descripcion = descripcion;
            this.fecha = fecha;
        }
        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getDescripcion() {

            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public Integer getNumero() {

            return numero;
        }

        public void setNumero(Integer numero) {
            this.numero = numero;
        }
    }
