package com.asl.model.validator;

import java.util.List;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.asl.entity.ListHead;
import com.asl.entity.Profile;
import com.asl.entity.Xusers;
import com.asl.service.ListService;
import com.asl.service.ProfileService;
import com.asl.service.XusersService;
/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public class ModelValidator extends ConstraintValidator {

	@Autowired private ListService listService;
	@Autowired private ProfileService profileService;
	@Autowired private XusersService xusersService;
//	@Autowired private ShopService shopService;

//	public void validateShop(Shop shop, Errors errors, Validator validator) {
//		if(shop == null) return;
//
//		super.validate(shop, errors, validator);
//		if (errors.hasErrors()) return;
//
//		// check sho number already exist
//		Shop ex = shopService.findByShopNumber(shop.getShopNo());
//		if(ex == null) return;
//
//		if(StringUtils.isBlank(shop.getHiddenShopNumber()) || !ex.getShopNo().equalsIgnoreCase(shop.getShopNo())) {
//			errors.rejectValue("shopNo", "Shop number already exist in the system");
//		}
//	}

//	public List<String> validateContact(Contact contact, Validator validator) {
//		List<String> errors = new ArrayList<>();
//		Set<ConstraintViolation<Contact>> constraintViolations = validator.validate(contact);
//		Iterator<ConstraintViolation<Contact>> it = constraintViolations.iterator();
//		while (it.hasNext()) {
//			ConstraintViolation<Contact> item = it.next();
//			if("resignDate".equalsIgnoreCase(item.getPropertyPath().toString())) continue;
//			errors.add(item.getPropertyPath().toString() + " -- " + item.getMessage());
//		}
//		return errors;
//	}

	public void validateXuser(Xusers xusers, Errors errors, Validator validator) {
		if(xusers == null || errors == null || validator == null) return;

		super.validate(xusers, errors, validator);
		if (errors.hasErrors()) return;

		// check admin user
		if("admin".equalsIgnoreCase(xusers.getZemail())) {
			errors.rejectValue("zemail", "You are not allowed to create system admin user");
			return;
		}

		// check username already exist
		if("Y".equalsIgnoreCase(xusers.getNewflag())) {
			List<Xusers> users = xusersService.findAllUserByZemail(xusers.getZemail());
			if(users != null && !users.isEmpty()) {
				errors.rejectValue("zemail", "Username already taken. Please try with another one");
				return;
			}
		}
		
		
	}

	public void validateListHead(ListHead listHead, Errors errors, Validator validator) {
		if(listHead == null || errors == null || validator == null) return;

		super.validate(listHead, errors, validator);
		if (errors.hasErrors()) return;

		// Check for duplicate listcode if this listhead has id
		ListHead lh = listService.findListHeadByListCode(listHead.getListCode());
		if(lh == null) return;

		if(listHead.getListHeadId() == null || (!listHead.getListHeadId().equals(lh.getListHeadId()))) {
			errors.rejectValue("listCode", "Listcode exist");
		}
	}

	public void validateProfile(Profile profile, Errors errors, Validator validator) {
		if(profile == null || errors == null || validator == null) return;

		super.validate(profile, errors, validator);
		if (errors.hasErrors()) return;

		// Check for duplicate profile if this profile has id
		Profile pr = profileService.findByProfileCodeAndProfileType(profile.getProfileCode().toUpperCase(), profile.getProfileType());
		if(pr == null) return;

		if(profile.getProfileId() == null || (!profile.getProfileId().equals(pr.getProfileId()))) {
			errors.rejectValue("profileCode", "Profile code exist");
		}
	}
}
