package com.tokio.prevencion.notificaciones73.portlet;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.prevencion.notificaciones73.constants.NotificacionesPrevencionPortletKeys;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;


@Component(immediate = true, property = { "javax.portlet.name="+ NotificacionesPrevencionPortletKeys.NOTIFICACIONESPREVENCION,
"mvc.command.name=/prevencion/notificaciones/getNotificaciones" }, service = MVCResourceCommand.class)

public class GetNotificacionesResourceCommand extends BaseMVCResourceCommand{

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		try{
			User user = (User) resourceRequest.getAttribute(WebKeys.USER);

			Gson gson = new Gson();
            String jsonString = gson.toJson(user);
            
            PrintWriter writer = resourceResponse.getWriter();
            writer.write(jsonString);
		}
		catch (Exception e) {
			// TODO: handle exception
			
		}
	}

}
