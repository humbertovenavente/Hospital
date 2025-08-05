import API_URL from "../config"; //  Importa tu configuración de URL

export async function descargarReporteExcel({ inicio, fin, limite, usuario }) {
  const params = new URLSearchParams({
    inicio,
    fin,
    limite,
    usuario,
  });

  const response = await fetch(`${API_URL}/reporte-medicinas/excel?${params.toString()}`);

  if (!response.ok) throw new Error("No se pudo generar el reporte");

  const blob = await response.blob();
  const url = window.URL.createObjectURL(blob);

  const a = document.createElement("a");
  a.href = url;
  a.download = "medicinas_reporte.xlsx";
  document.body.appendChild(a); // Mejor agregar el enlace al body (opcional)
  a.click();
  document.body.removeChild(a);
  window.URL.revokeObjectURL(url);
}
