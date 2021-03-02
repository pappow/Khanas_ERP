/**
 * Application basepath
 */
function getBasepath(){
	var basePath = $('a.basePath').attr('href');
	basePath = basePath.split('/')[1];
	var href = location.href.split('/');
	if(basePath != ''){
		return href[0] + '//' + href[2] + '/' + basePath;
	}
	return href[0] + '//' + href[2];
}

/**
 * Show success message
 * @param message
 * @returns success message bar
 */
function showSuccess(message){
	showScreenMessage('alert-success', message, 'Success!');
}

/**
 * Show error message
 * @param message
 * @returns success message bar
 */
function showError(message){
	showScreenMessage('alert-danger', message, 'Error!');
}

function showScreenMessage(type, message, identifier){
	$('.ck-alert').removeClass('nodisplay');
	$('.ck-alert').addClass(type);
	$('.ck-alert-identifier').html(identifier)
	$('.ck-alert-message').html(message);
	setTimeout(() => {
		$('.ck-alert-identifier').html("")
		$('.ck-alert-message').html("");
		$('.ck-alert').removeClass(type);
		$('.ck-alert').addClass('nodisplay');
	}, 4000);
}



/**
 * Show lobi message
 * @param type
 * @param message
 * @returns
 */
function showMessage(type, message){
	Lobibox.notify(type, {
		title: type,
		pauseDelayOnHover: true,
		continueDelayOnInactiveTab: true,
		size: 'mini',
		rounded: false,
		delayIndicator: true,
		sound: false,
		position: 'right top',
		msg: message
	});
}


/**
 * Loading mask object
 * function1 : show  -- Show loading mask
 * function2 : hide  -- Hide loading mask
 */
var loadingMask = {
		show : function(){
			$('div.loading-mask').removeClass('nodisplay');
		},
		hide : function(){
			$('div.loading-mask').addClass('nodisplay');
		}
	}
var loadingMask2 = {
		show : function(){
			$('div#loadingmask2, div.loadingdots, div#loadingdots').removeClass('nodisplay');
		},
		hide : function(){
			$('div#loadingmask2, div.loadingdots, div#loadingdots').addClass('nodisplay');
		}
	}


/**
 * Trigger update button
 * @returns
 */
function triggerUpdateButton(){
	console.log('%cTrigger update button...', 'color: green');

	$('li.editmode').addClass('nodisplay');
	$('a.btn.editmode').addClass('nodisplay');
	$('li.viewmode').removeClass('nodisplay');
	$('a.btn.viewmode').removeClass('nodisplay');
	$('#mainform .form-control, #mainform .form-control-input2').removeAttr('disabled');

	if($('table.datatable').length > 0){
		$('table.datatable').each(function (tindex, table) {
			$(table).find('th button.editmode').removeAttr('disabled');
			var datatable = $(table).DataTable();
			datatable.rows().every(function(index, element) {
				var row = $(this.node());
				$(row).find('button.editmode').removeAttr('disabled');
			});
		});
	}

	$('input.form-control-input2[disabled!="disabled"]').parent().css("margin-left", "0px");
}
function triggerUpdateButtonSpecific(tableId){
	console.log('%cTrigger update button...', 'color: green');

	$('li.editmode').addClass('nodisplay');
	$('a.btn.editmode').addClass('nodisplay');
	$('li.viewmode').removeClass('nodisplay');
	$('a.btn.viewmode').removeClass('nodisplay');
	$('#mainform .form-control, #mainform .form-control-input2').removeAttr('disabled');

	if($('table#' + tableId).length > 0){
		var table = $('table#' + tableId);
		$(table).find('th button.editmode').removeAttr('disabled');
		var datatable = $(table).DataTable();
		datatable.rows().every(function(index, element) {
			var row = $(this.node());
			$(row).find('button.editmode').removeAttr('disabled');
		});
	}

	$('input.form-control-input2[disabled!="disabled"]').parent().css("margin-left", "0px");
}

/**
 * Trigger Cancel Button
 */
function triggerCancelButton(){
	console.log('%cTrigger cancel button...', 'color: green');
	var href = location.href;
	if(href.lastIndexOf('copy') != -1){
		href = href.slice(0,-5)
		console.log(href);
		window.location.replace(href);
		return;
	}
	location.reload(true);
}

/**
 * Modal loader
 * @param url
 * @param modalid
 * @returns
 */
function modalLoader(url, modalid){
	$.ajax({
		url : url,
		type : 'GET',
		success : function(data){
			$('#' + modalid + '-section').html(data);
			$('#' + modalid).modal({backdrop: 'static', keyboard: false}, 'show');
		},
		error : function(jqXHR, status, errorThrown){
			showMessage(status, "Something went wrong .... ");
		},
	});
}

/**
 * Data table buttons event binder
 * @returns
 */
function bindDataTableButtonsEvent(datatable){
	// New button
	$('table').find('button.btn-add').off('click').on('click', function(e){
		e.preventDefault();

		var url = $(this).data('url');
		var modalid = $(this).data('target-modal');
		modalLoader(url, modalid);
	});

	if(datatable){
		datatable.rows().every(function(index, element) {
			var row = $(this.node());

			// Edit button
			$(row).find('button.btn-edit').off('click').on('click', function(e){
				e.preventDefault();
				var url = $(this).data('url');
				var modalid = $(this).data('target-modal');
				modalLoader(url, modalid);
			});

			// Delete button
			$(row).find('button.btn-delete').off('click').on('click', function(e){
				var url = $(this).data('url');
				if(confirm("Are you sure you want to delete this item?")){
					doItemDelete(url);
				}
			});
		});
	}
}

/**
 * Bind normal table button event
 */
function bindTableButtonsEvent(targetTable){
	console.log('%cBind normal table button event', 'color: green');
	// New or edit button
	$(targetTable).find('button.btn-edit, button.btn-add').off('click').on('click', function(e){
		e.preventDefault();

		var url = $(this).data('url');
		var modalid = $(this).data('target-modal');
		modalLoader(url, modalid);
	});

	// Delete button
	$(targetTable).find('button.btn-delete').off('click').on('click', function(e){
		var url = $(this).data('url');
		if(confirm("Are you sure you want to delete this item?")){
			doItemDelete(url);
		}
	});
}


/**
 * Data table item delete
 * @param url
 * @returns
 */
function doItemDelete(url){
	console.log('%cTrigger delete item ... ', 'color: red');

	$.ajax({
		url : url,
		type : 'POST',
		beforeSend : loadingMask2.show(),
		success : function(data) {
			console.log({data});
			if(data.status == 'SUCCESS'){
				showMessage(data.status.toLowerCase(), data.message);
				if(data.reloadurl){
					doSectionReloadWithNewData(data);
				}
			} else {
				showMessage(data.status.toLowerCase(), data.message);
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			showMessage(status, "Something went wrong .... ");
		},
		complete: loadingMask2.hide()
	});
	
}


/**
 * Data table init
 * @returns
 */
function dataTableInit(){
	console.log('%cDataTable init.. ', 'color: green');
	$('table.datatable').each(function (tindex, table) {
		var noSortColumns = [];
		$(table).find('th[data-nosort="Y"]').each(function(i, col){
			noSortColumns.push($(col).index());
		});

		var datatable = $(table).DataTable({
			"columnDefs": [{
				"targets": noSortColumns,
				"orderable": false
			}],
			"responsive": true
		});

		new $.fn.dataTable.FixedHeader(datatable);

		bindDataTableButtonsEvent(datatable);
	});
}

function dataTableInitSpecific(tableId){
	console.log('%cDataTable init.. ', 'color: green');
	$('table#' + tableId).each(function (tindex, table) {
		var noSortColumns = [];
		$(table).find('th[data-nosort="Y"]').each(function(i, col){
			noSortColumns.push($(col).index());
		});

		var datatable = $(table).DataTable({
			"columnDefs": [{
				"targets": noSortColumns,
				"orderable": false
			}],
			"responsive": true
		});

		new $.fn.dataTable.FixedHeader(datatable);

		bindDataTableButtonsEvent(datatable);
	});
}




/**
 * Submit main form
 * @param customurl
 * @returns
 */
function submitMainForm(customurl){
	if($('form#mainform').length < 1) return;
	console.log('%cForm submit triggered', 'color: green');

	var targettedForm = $('form#mainform');
	console.log('%cValidting form','color: green');
	if(!targettedForm.smkValidate()) return;

	var submitUrl = (customurl != undefined) ? customurl : targettedForm.attr('action');
	var submitType = targettedForm.attr('method');
	var formData = $(targettedForm).serializeArray();
	var enctype = targettedForm.attr('enctype');
	if(enctype == 'multipart/form-data'){
		submitMultipartForm(submitUrl, submitType);
		return;
	}

	console.log('%cUrl : ' + submitUrl + ', Type : ' + submitType,'color: green');
	console.log({formData});

	$.ajax({
		url : submitUrl,
		type :submitType,
		data : formData,
		beforeSend : loadingMask2.show(),
		success : function(data) {
			console.log({data});
			if(data.status == 'SUCCESS'){
				showMessage(data.status.toLowerCase(), data.message);
				if(data.reloadurl){
					doSectionReloadWithNewData(data);
				} else if(data.redirecturl){
					setTimeout(() => {
						window.location.replace(getBasepath() + data.redirecturl);
					}, 1500);
				}
			} else {
				showMessage(data.status.toLowerCase(), data.message);
			}
			loadingMask2.hide();
		}, 
		error : function(jqXHR, status, errorThrown){
			showMessage(status, "Something went wrong .... ");
			loadingMask2.hide();
		}
	});
}

/**
 * Multipart file upload
 * @param submitUrl
 * @param submitType
 * @returns
 */
function submitMultipartForm(submitUrl, submitType){
	var files = $('#fileuploader').get(0).files;
	if (files.length == 0){
		alert("No files selected to upload");
		return;
	}

	var formData = new FormData();
	for (var x = 0; x < files.length; x++) {
		formData.append("files[]", files[x]);
	}

	$.ajax({
		url : submitUrl,
		type :submitType,
		data : formData,
		async: false,
		cache: false,
		processData: false,
		contentType: false,
		beforeSend : loadingMask2.show(),
		success : function(data) {
			console.log({data});
			if(data.status == 'SUCCESS'){
				showMessage(data.status.toLowerCase(), data.message);
				if(data.redirecturl){
					setTimeout(() => {
						window.location.replace(getBasepath() + data.redirecturl);
					}, 1500);
				}
			} else {
				showMessage(data.status.toLowerCase(), data.message);
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			showMessage(status, "Something went wrong .... ");
		},
		complete: loadingMask2.hide(),
	});
}

/**
 * Submit Report form
 * @param customurl
 * @returns
 */
function submitReportForm(customurl){
	if($('form#reportform').length < 1) return;
	console.log('%cForm submit triggered', 'color: green');

	var targettedForm = $('form#reportform');
	console.log('%cValidting form','color: green');
	if(!targettedForm.smkValidate()) return;

	var submitUrl = (customurl != undefined) ? customurl : targettedForm.attr('action');
	var submitType = targettedForm.attr('method');
	var formData = $(targettedForm).serializeArray();
	var reportType = $('#reportType').val();
	var reportName = $('#reportName').val() != '' ? $('#reportName').val() : 'report';

	$.ajax({
		url : submitUrl,
		type : submitType,
		data : formData,
		beforeSend : loadingMask2.show(),
		success : function(data) {
			loadingMask2.hide();
			var arrrayBuffer = base64ToArrayBuffer(data);
			if("PDF" == reportType){
				var blob = new Blob([arrrayBuffer], {type: "application/pdf"});
				var link = window.URL.createObjectURL(blob);
				window.open(link,'', 'height=650,width=840');
			} else {
				var blob = new Blob([arrrayBuffer], {type: "application/octetstream"});
				var isIE = false || !!document.documentMode;
				if (isIE) {
					window.navigator.msSaveBlob(blob, reportName + ".xls");
				} else {
					var url = window.URL || window.webkitURL;
					link = url.createObjectURL(blob);
					var a = $("<a />");
					a.attr("download", reportName + ".xls");
					a.attr("href", link);
					$("body").append(a);
					a[0].click();
					$(a, "body").remove();
				}
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			loadingMask2.hide();
			showMessage(status, "Something went wrong .... ");
		}
	});
}

/**
 * Convert Base64 string to array buffer
 * @param base64
 * @returns
 */
function base64ToArrayBuffer(base64) {
	var binaryString = window.atob(base64);
	var binaryLen = binaryString.length;
	var bytes = new Uint8Array(binaryLen);
	for (var i = 0; i < binaryLen; i++) {
		var ascii = binaryString.charCodeAt(i);
		bytes[i] = ascii;
	}
	return bytes;
}

/**
 * Submit modal main form
 * @param customurl
 * @returns
 */
function submitModalForm(customurl){
	if($('form#mainform-modal').length < 1) return;
	console.log('%cForm submit triggered from modal', 'color: green');

	var targettedForm = $('form#mainform-modal');
	console.log('%cValidting form','color: green');
	if(!targettedForm.smkValidate()) return;

	var submitUrl = (customurl != undefined) ? customurl : targettedForm.attr('action');
	var submitType = targettedForm.attr('method');
	var formData = $(targettedForm).serializeArray();
	console.log('%cUrl : ' + submitUrl + ', Type : ' + submitType,'color: green');
	console.log({formData});

	$.ajax({
		url : submitUrl,
		type :submitType,
		data : formData,
		beforeSend : loadingMask2.show(),
		success : function(data) {
			console.log({data});
			if(data.status == 'SUCCESS'){
				$('div.modal').modal('hide');
				showMessage(data.status.toLowerCase(), data.message);
				if(data.redirecturl){
					setTimeout(() => {
						window.location.replace(getBasepath() + data.redirecturl);
					}, 1500);
				} else if(data.reloadurl){
					doSectionReloadWithNewData(data);
				}
			} else {
				showMessage(data.status.toLowerCase(), data.message);
			}
			loadingMask2.hide()
		}, 
		error : function(jqXHR, status, errorThrown){
			showMessage(status, "Something went wrong .... ");
			loadingMask2.hide()
		}
	});
}

/**
 * Section reloader from form submitted returned data
 * @param rdata
 * @returns
 */
function doSectionReloadWithNewData(rdata){
	$.ajax({
		url : getBasepath() + rdata.reloadurl,
		type : 'GET',
		beforeSend : loadingMask2.show(),
		success : function(data) {
			console.log({data});
			var wrapperelement = $('#' + rdata.reloadelementid + "_wrapper");
			if($(wrapperelement).length > 0){
				wrapperelement.html("");
				wrapperelement.append(data);

				//dataTableInit();
				dataTableInitSpecific(rdata.reloadelementid);
				triggerUpdateButtonSpecific(rdata.reloadelementid);
				//triggerUpdateButton();
			} else {
				console.log("Normal table");
				var target = $('#' + rdata.reloadelementid);
				var parentElement = target.parent();
				parentElement.html("");
				parentElement.append(data);
				bindTableButtonsEvent($('#' + rdata.reloadelementid));
			}
			loadingMask2.hide()
		}, 
		error : function(jqXHR, status, errorThrown){
			showMessage(status, "Something went wrong .... ");
			loadingMask2.hide()
		}
	});
};

