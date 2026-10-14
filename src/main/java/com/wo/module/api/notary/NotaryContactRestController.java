package com.wo.module.api.notary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wo.module.notary.model.Notary;
import com.wo.module.notary.service.NotaryService;

@RestController
@RequestMapping("/notary/contact")
public class NotaryContactRestController {

	private final NotaryService notaryService;

	@Autowired
	public NotaryContactRestController(@Qualifier("notaryService") NotaryService notaryService) {
		this.notaryService = notaryService;
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public NotaryContactResponse updateContact(@RequestBody NotaryContactUpdateRequest request) {
		String validationMessage = NotaryContactValidator.validate(request);
		if (validationMessage != null) {
			throw new NotaryContactValidationException(validationMessage);
		}
		Notary updated = notaryService.updateEmailByPhone(request.getPhoneNo(), request.getEmail());
		if (updated == null) {
			throw new NotaryContactNotFoundException(request.getPhoneNo());
		}
		return NotaryContactResponse.from(updated);
	}
}
