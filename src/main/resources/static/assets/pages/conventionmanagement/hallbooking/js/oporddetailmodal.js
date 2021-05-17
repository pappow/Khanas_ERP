$(document).ready(function(){
	// Get item purchase unit
	var hallitems = {};
	var items = [];
	var functionsUK = [];

	$('button.item-box').off('click').on('click', function(e){
		var xitem = $(this).data('xitem');
		var xcatitem = $(this).data('xcatitem');

		if(items.includes(xitem)){
			if(xcatitem == 'Function'){
				if(functionsUK.includes(xitem)){
					functionsUK.pop(xitem);
				}
			}

			const index = items.indexOf(xitem);
			if (index > -1) {
				items.splice(index, 1);
			}
			$(this).toggleClass("btn-success btn-ddefault");
			console.log({items});
			calculateRate();
			return;
		}


		if(xcatitem == 'Function'){
			if(functionsUK.length > 0){
				var rxitem = functionsUK[0];
				const index = items.indexOf(rxitem);
				if (index > -1) {
					items.splice(index, 1);
				}
				functionsUK.pop();
				$('.' + rxitem).toggleClass("btn-success btn-ddefault");
			}
			functionsUK.push(xitem);
		}
		items.push(xitem);


		$(this).toggleClass("btn-success btn-ddefault");

		console.log({items});
		calculateRate();
	})

	function calculateRate(){
		var total = 0;
		items.forEach(function(item, index){
			total += $('.' + item).data('xrate');
		});
		$('.rate-btn').html("Total : " + total + "/-");
	}

	$('.confirm-btn-modal').off('click').on('click', function(e){
		e.preventDefault();

		console.log({items});

		loadingMask2.show();
		$.ajax({
			url : getBasepath() + '/conventionmanagement/hallbooking/oporddetails/save',
			type : 'POST',
			dataType: 'json',
			data: JSON.stringify(items),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
			},
			success : function(data) {
				loadingMask2.hide();	
				console.log({data});
			},
			error : function(jqXHR, status, errorThrown){
				showMessage(status, "Something went wrong .... ");
				loadingMask2.hide();
			}
		});
		
	})
});