<template>
    <div class="container">
      <h2>Mis Pagos y Facturación</h2>
      <table class="payment-table">
        <thead>
          <tr>
            <th>Fecha</th>
            <th>Servicio</th>
            <th>Monto</th>
            <th>Método de Pago</th>
            <th>Estado</th>
            <th>Factura</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pago in pagos" :key="pago.id">
            <td>{{ pago.fecha }}</td>
            <td>{{ pago.servicio }}</td>
            <td>Q {{ pago.monto.toFixed(2) }}</td>
            <td>{{ pago.metodoPago }}</td>
            <td>
              <span :class="{'status-paid': pago.estado === 'Pagado', 'status-pending': pago.estado === 'Pendiente'}">
                {{ pago.estado }}
              </span>
            </td>
            <td>
              <button class="btn-download" @click="descargarFactura(pago.id)">Descargar PDF</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>

  <script>
  export default {
    data() {
      return {
        pagos: [
          { id: 101, fecha: "05/03/2025", servicio: "Consulta General", monto: 500, metodoPago: "Tarjeta", estado: "Pagado" },
          { id: 102, fecha: "20/02/2025", servicio: "Rayos X", monto: 850, metodoPago: "Seguro", estado: "Pagado" },
          { id: 103, fecha: "15/02/2025", servicio: "Examen de Sangre", monto: 250, metodoPago: "Efectivo", estado: "Pendiente" },
          { id: 104, fecha: "10/02/2025", servicio: "Terapia Física", monto: 1200, metodoPago: "Seguro", estado: "Pagado" }
        ]
      };
    },
    methods: {
      descargarFactura(id) {
        alert(`Descargando factura para el pago #${id}...`);
        // Aquí podrías implementar la generación del PDF
      }
    }
  };
  </script>

  <style scoped>
  .container {
    width: 90%;
    margin: 20px auto;
    padding: 20px;
    background-color: #f3f4f6;
    border-radius: 8px;
    box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1);
  }

  h2 {
    text-align: center;
    margin-bottom: 15px;
    color: #2c3e50;
  }

  .payment-table {
    width: 100%;
    border-collapse: collapse;
    background: white;
    border-radius: 8px;
    overflow: hidden;
  }

  th, td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }

  th {
    background-color: #34495e;
    color: white;
  }

  .status-paid {
    color: green;
    font-weight: bold;
  }

  .status-pending {
    color: red;
    font-weight: bold;
  }

  .btn-download {
    background-color: #2ecc71;
    color: white;
    border: none;
    padding: 5px 10px;
    cursor: pointer;
    border-radius: 4px;
  }

  .btn-download:hover {
    background-color: #27ae60;
  }
  </style>
