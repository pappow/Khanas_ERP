$(document).ready(function(){
	console.log("%cSearch Sugget Init...", "color:green");

	$(".searchsuggest").off('keyup').on('keyup', debounce(function() {

		var hint = $(this).val();
		var targetElement = $(this);
		var parent = $(this);
		var serachUrl = $(this).attr('search-url');

		if(hint == '') return;

		$.ajax({
			url : getBasepath() + '/' + serachUrl + '/' + hint,
			type : 'GET',
			success : function(data) {
				generateSearchResult(targetElement, data);
			},
			error : function(jqXHR, status, errorThrown){
				//showMessage(status, "Something went wrong .... ");
			}
		});

	}, 1000));

	$(".searchsuggest").off('blur').on('blur', function(){
		var targetElement = $(this);
		var parent = $(targetElement).parent();

		// Remove previous search suggest div if exist
		setTimeout(() => {
			if($(parent).find('.search-suggest-results').length > 0){
				$(parent).find('.search-suggest-results').remove();
			}
			if($(targetElement).val() == ''){
				$(parent).find('#search-val').val("");
				$(parent).find('#search-des').val("");
			} else {
				$(targetElement).val($(parent).find('#search-des').val());
			}
		}, 500);
	})

	$(".searchsuggest").off('focus').on('focus', function(){
//		$(this).val("");
//		var parent = $(this).parent();
//		$(parent).find('#search-val').val("");
//		$(parent).find('#search-des').val("");
	})

	function generateSearchResult(uielement, data){
		var parent = $(uielement).parent();

		// Remove previous search suggest div if exist
		if($(parent).find('.search-suggest-results').length > 0){
			$(parent).find('.search-suggest-results').remove();
		}

		// create new search suggest resul div
		$(parent).append('<div class="search-suggest-results col-sm-6"></div>');

		var searchContainer = $(parent).find('.search-suggest-results');
		$(searchContainer).css({
			'display':'block',
			'min-width' : $(uielement).width() + 25,
			'top' : $(uielement).height("px")
		});

		// clreate list items
		$(searchContainer).html('<ul class="search-container-ul"></ul>');
		var totalItem = 0;
		$.each(data, function(index, item){
			totalItem++;
			var listitem = '<li class="search-item" value="'+ item.value +'" prompt="'+ item.prompt +'">'+ item.prompt +'</li>';
			$(searchContainer).find('.search-container-ul').append(listitem);
		})
		if(totalItem == 0){
			var noresultitem = '<li class="search-item" value="" prompt="">No result found</li>';
			$(searchContainer).find('.search-container-ul').append(noresultitem);
		}

		$(searchContainer).find('.search-item').off('click').on('click', function(){
			var itemprompt = $(this).attr('prompt');
			var itemval = $(this).attr('value');
			$(uielement).val(itemprompt);
			$(parent).find('#search-val').val(itemval);
			$(parent).find('#search-des').val(itemprompt);
			$(searchContainer).remove();
		})

	}

	function debounce(func, wait, immediate) {
		var timeout;
		return function() {
			var context = this, args = arguments;
			var later = function() {
				timeout = null;
				if (!immediate) func.apply(context, args);
			};
			var callNow = immediate && !timeout;
			clearTimeout(timeout);
			timeout = setTimeout(later, wait);
			if (callNow) func.apply(context, args);
		};
	};
});