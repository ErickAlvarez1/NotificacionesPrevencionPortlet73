package com.tokio.prevencion.notificaciones73.portlet;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.tokio.prevencion.prevencionservicebuilder.model.Shipmentdata;
import com.tokio.prevencion.prevencionservicebuilder.model.Solicitud_Servicios_Prevencion_Transporte_Carga;
import com.tokio.prevencion.prevencionservicebuilder.model.Usuario;
import com.tokio.prevencion.prevencionservicebuilder.service.ShipmentdataLocalService;
import com.tokio.prevencion.prevencionservicebuilder.service.Solicitud_Servicios_Prevencion_Transporte_CargaLocalService;
import com.tokio.prevencion.prevencionservicebuilder.service.UsuarioLocalService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


/**
 * @author martinfernandojimenezramirez
 */
@ApplicationPath("/prevencion/notificaciones")
@Component(immediate = true, service = Application.class)
public class GetNotificacionesRest extends Application {
	
	@Reference
	Solicitud_Servicios_Prevencion_Transporte_CargaLocalService _Solicitud_Servicios_Prevencion_Transporte_CargaLocalService;
	
	@Reference
	ShipmentdataLocalService _ShipmentdataLocalService;
	
	@Reference
	UsuarioLocalService _UsuarioLocalService;

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	@GET
	@Produces("text/plain")
	public String working() {
		return "It works!";
	}
	
	@GET
	@Path("/byusuarioid/{userid}")
	@Produces("json/application")
	public String byusuarioid(
		@PathParam("userid") long userid) {
		Gson gson = new Gson();
		
		//User user = UserLocalServiceUtil.fetchUserById(userId)
		List<Role> roles = RoleLocalServiceUtil.getUserRoles(userid);
		
		List<Solicitud_Servicios_Prevencion_Transporte_Carga> sols = new ArrayList<Solicitud_Servicios_Prevencion_Transporte_Carga>();
		List<Shipmentdata> ships = new ArrayList<Shipmentdata>();
		List<Usuario> usrs = new ArrayList<Usuario>();
		List<Solicitud_Servicios_Prevencion_Transporte_Carga> plan = new ArrayList<Solicitud_Servicios_Prevencion_Transporte_Carga>();
		for (Role role : roles) {
			String roleName = role.getName().toUpperCase();
			if (roleName.equals("TMX-CONSULTOR")) {
				sols = _Solicitud_Servicios_Prevencion_Transporte_CargaLocalService.getSinFecha(userid);
				ships = _ShipmentdataLocalService.getShipmentdatasByUser(userid);
				usrs = _UsuarioLocalService.getSinAsignarByConsultor(userid);
				plan = _Solicitud_Servicios_Prevencion_Transporte_CargaLocalService.getSolicitudesByPlanConfirmado(userid);
			}
			if (roleName.equals("TMX-ADMIN"))
				sols = _Solicitud_Servicios_Prevencion_Transporte_CargaLocalService.getSinConsultor();
		}
		Map<String, Object> notis = new HashMap<String, Object>();
		notis.put("sols", sols);
		notis.put("ships", ships);
		notis.put("usrs", usrs);
		notis.put("plan", plan);
		return gson.toJson(notis);
	}
}