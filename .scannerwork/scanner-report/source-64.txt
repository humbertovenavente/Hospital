<template>
  <div class="container">
    <div class="header">Historial de pagos y facturación</div>

    <div class="section">
    <table >
      <thead>
        <tr>
          <th>ID Pago</th>
          <th>Fecha</th>
          <th>Paciente</th>
          <th>Monto</th>
          <th>Detalles del Servicio</th>
          <th>Método de Pago</th>
          <th>Estado</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="pago in pagos" :key="pago.id">
          <td>{{ pago.id }}</td>
          <td>{{ pago.fecha }}</td>
          <td>{{ pago.paciente }}</td>
          <td>Q {{ pago.monto.toFixed(2) }}</td>
          <td>{{ pago.servicio }}</td>
          <td>{{ pago.metodoPago }}</td>
          <td>
            <span :class="{'status-paid': pago.estado === 'Pagado', 'status-pending': pago.estado === 'Pendiente'}">
              {{ pago.estado }}
            </span>
          </td>
          <td>
            <button class="btn-detail">Detalle</button>
            <button class="btn-invoice">Generar Factura</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</template>

<script>
export default {
  data() {
    return {
      pagos: [
        { id: 1, fecha: "20/02/2025", paciente: "Juan Pérez López", monto: 500, servicio: "Consulta General", metodoPago: "Tarjeta", estado: "Pagado" },
        { id: 2, fecha: "15/02/2025", paciente: "María Gómez Ruiz", monto: 1200, servicio: "Cirugía de Úlcera", metodoPago: "Seguro", estado: "Pagado" },
        { id: 3, fecha: "10/02/2025", paciente: "Carlos Méndez Díaz", monto: 800, servicio: "Tratamiento de Fractura", metodoPago: "Efectivo", estado: "Pendiente" },
        { id: 4, fecha: "05/02/2025", paciente: "Ana López Herrera", monto: 350, servicio: "Chequeo General", metodoPago: "Tarjeta", estado: "Pagado" },
        { id: 5, fecha: "01/02/2025", paciente: "Pedro Sánchez Jiménez", monto: 700, servicio: "Terapia Respiratoria", metodoPago: "Seguro", estado: "Pagado" }
      ]
    };
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
  background: #f9f9f9;
  color: #e0e1dd;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.header {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  background: #45C4B0;
  color: white;
  padding: 12px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}

.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
}

.payment-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

th, td {
  padding: 12px;
  border: 1px solid #DAFDBA;
  text-align: center; /* Alinear al centro */
  vertical-align: middle; /* Centrar contenido verticalmente */

}

th {
  background: #01324b;
}

.status-paid {
  color: #DAFDBA;
  font-weight: bold;
}

.status-pending {
  color: #ff8a7d;
  font-weight: bold;
}



.btn-detail, .btn-invoice {
  padding: 5px 10px;
  margin: 2px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.btn-detail {
  background-color: #f0ad4e;
  color: white;
}

.btn-invoice {
  background-color: #DAFDBA;
  color: #012030;
}

.btn-detail:hover {
  background-color: #f0ad4e;
}

.btn-invoice:hover {
  background-color: #DAFDBA;
  color: #012030;
}
</style>
