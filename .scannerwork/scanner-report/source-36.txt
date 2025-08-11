import API_URL from "../config"; //  Importa tu configuración de URL

export async function descargarReporteModeracionExcel({ fechaInicio, fechaFin, limite }) {
    const params = new URLSearchParams({
      fechaInicio,
      fechaFin,
      limite,
    });

    const response = await fetch(`${API_URL}/api/reporte-moderacion/usuarios/excel?${params.toString()}`);

    if (!response.ok) throw new Error("No se pudo generar el reporte");

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = "reporte_usuarios_moderacion.xlsx";
    document.body.appendChild(a); //  opcional: mejor compatibilidad
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
}
