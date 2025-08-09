import emailjs from "emailjs-com";

const SERVICE_ID = "service_f70s6q3";
const TEMPLATE_ID = "template_tf3o0fd";
const PUBLIC_KEY = "SFAQ9kOAKVFMBgkSC";

export const sendWelcomeEmail = async (userEmail, userName) => {
  try {
    const templateParams = {
      to_email: userEmail,
      to_name: userName,
    };

    await emailjs.send(SERVICE_ID, TEMPLATE_ID, templateParams, PUBLIC_KEY);
    console.log("Correo de bienvenida enviado con éxito");
  } catch (error) {
    console.error("Error enviando correo:", error);
  }
};
