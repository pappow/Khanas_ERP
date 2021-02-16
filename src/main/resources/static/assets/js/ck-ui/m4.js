/**
 @module Metaboot
 */
/**
 * @class Date Picker
 */

/**
 * Initialises datepicker widget on any inputs with class of input.con-datepicker, input.con-startdate, input.con-enddate, input.con-futuredate, input.con-pastdate
 * @method datepickerInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @param {object} m4language Required global variable from screen specific config.js. Both the datepicker plugin and moment.js have language formats settings that are loaded for each plugin respectively from the config file.
 * @param {object} m4dateformat Required global variable from screen specific config.js. Both the datepicker plugin and moment.js have seperate date format settings that are loaded for each plugin respectively from the config file.
 * @version
 * @author Tom Yeldham
 */

//DATEPICKERS

function datepickerInitialisation(target) {

    // Initialize datepickers with the DD-MMM-YYYY format (note that the addon uses dd-M-yyyy to generate that format)
    $('input.con-datepicker, input.con-startdate, input.con-enddate, input.con-futuredate, input.con-pastdate', target).each(function() {
        var datepickerformat;
        if ($(this).data('date-format') === undefined) {
            datepickerformat = m4dateformat.datepickers;
        }
        else {
            datepickerformat = $(this).data('date-format');
        }
        var momentformat;
        if ($(this).data('moment-format') === undefined) {
            momentformat = m4dateformat.moment;
        }
        else {
            momentformat = $(this).data('moment-format');
        }
        $(this).datepicker({
            orientation: "auto",
            format: datepickerformat,
            autoclose: false,
            language: "m4"
        }).on("changeDate", function() {
            // Then add validation triggers on the changedate event.
            // avoid validation for input.con-startdate, input.con-enddate as those will be validated on blur event
            if ($(this).is('input.con-startdate, input.con-enddate') === false) {
                if ($('div.datepicker:visible').length > 0) {
                    $(this).valid();
                }
            }
            if ($(this).parents('div#flightstable')) {
                //Specific event for the booking screen flight modal datepickers
                $(this).trigger('nowdodatepickerstuff');
            }
            if ($('div.datepicker:visible').length > 0) {
                $(this).focus();
            }
        }).on('show', function() {
            // Make sure that on datepicker opening, the selected date is correctly shown
            $(this).datepicker('update');
        }).on("blur", function() {
            if ($('div.datepicker:visible').length < 1 && ($(this).val() !== "" || $(this).data("beenactive") === "Y")) {
                var thisval = $(this).val();
                var m4date;
                //Get the numbers from the value
                var numbersinvalue = thisval.replace(/[\.,-\/#!$ï¿½%\^& \*;:{}=\-_`~()a-zA-Z]/g, '');
                //Check for value existance and the presence of at least 2 digits
                if (!$(this).valid()) {
                    if (thisval.length > 0 && numbersinvalue.length >= 1) {
                        //If so make pull the letters from the value
                        var lettersinvalue = thisval.replace(/[\.-\/#!$ï¿½%\^& \*;:{}=\-_`~()0-9]/g, '');
                        //Make an array of the words, split at comma's
                        var wordarray = lettersinvalue.split(",");
                        var datepickermatcher = 0;
                        var wordarraycounter = 0;
                        //If there are contents in the array
                        if (lettersinvalue.length > 0) {
                            //Loop each one
                            $.each(wordarray, function() {
                                var strippedword = wordarray[wordarraycounter].replace(/[\.,-\/#!$ï¿½%\^& \*;:{}=\-_`~()]/g, '');
                                //Capitalise the first letter of each item
                                var capitalisedword = capitaliseFirstLetter(strippedword);
                                wordarray[wordarraycounter] = capitalisedword;
                                //If the contents match the global variable months or days
                                if ($.inArray(wordarray[wordarraycounter], m4language.moment.monthsShort) !== -1) {
                                    //Increase the ticker
                                    datepickermatcher++;
                                    if ($.inArray(wordarray[wordarraycounter], m4language.moment.weekdaysShort) !== -1)
                                    {
                                        datepickermatcher++;
                                    }
                                }
                                wordarraycounter++;
                            });
                        }
                        //If there are no matches
                        if (datepickermatcher < 1) {
                            // Check if theres any letters left in the value
                            if (thisval.replace(/[\.-\/#!$ï¿½%\^& \*;:{}=\-_`~()0-9]/g, '').length === 0) {
                                var strippedval = thisval.replace(/[\.,-\/#!$ï¿½%\^& \*;:{}=\-_`~()]/g, '');
                                //Check for ddmmyy format
                                if (strippedval.length === 6) {
                                    var typedyears = strippedval.substr(4);
                                    //If ddmmyy then check if the years were typed as 00
                                    if (typedyears === "00") {
                                        //If so then set the years as 2000 
                                        var daysandmonths = thisval.substring(0, 4);
                                        thisval = daysandmonths + "2000";
                                    }
                                }
                                //Set the date variable using the created string in the format it was produced in
                                m4date = moment(thisval, "DD-MM-YYYY");
                            }
                            else {
                                // Call Fire validation error
                                $(this).valid();
                                return false;
                            }
                        }
                        else {
                            // if there are 2 matches, then it is using ddd, DD-MMM-YYYY format
                            if (datepickermatcher === 2) {
                                m4date = moment(thisval, "ddd, DD-MMM-YYYY");
                            }
                            else {
                                m4date = moment(thisval, "DD-MMM-YYYY");
                            }
                        }
                        var dyears = m4date.format("YYYY");
                        // If no years are typed into the field
                        if (dyears === "0000") {
                            //Sets the years to the current year
                            var today = moment().format("YYYY-MM-DD");
                            var fulldate = m4date.year(moment().format("YYYY"));
                            var usablefulldate = fulldate.format("YYYY-MM-DD");
                            //If the full date has occured this year, then guess next year
                            if (moment(usablefulldate).isBefore(today)) {
                                usablefulldate = moment(usablefulldate).add('years', 1);
                            }
                            //Format the date with the global date format
                            var finaldate = moment(usablefulldate).format(momentformat);
                            $(this).val(finaldate);
                        }
                        else {
                            //Checks if it is a 4 character date that has been typed
                            if (dyears.charAt(0) === "0") {
                                //if not then creates variables for the year 10 years from now, the last 2 values of that year and of the typed date
                                var tenyearsahead = moment().add("years", 10).format("YYYY");
                                var trimmedtenyearsahead = tenyearsahead.substr(2);
                                var trimmedyears = dyears.substr(2);
                                var correctedyear;
                                // if the last 2 digits of the typed year value is greater than those of the date 10 years from now, then assume that the date is meant to be in the past and set to 19+these digits
                                if (trimmedyears > trimmedtenyearsahead) {
                                    correctedyear = 19 + trimmedyears;
                                }
                                else {
                                    //otherwise guess 20..
                                    correctedyear = 20 + trimmedyears;
                                }
                                //use this calculated year value to combine with the rest of the typed date to form a final date
                                var usabled = m4date.format(momentformat);
                                var trimmed = usabled.substring(0, usabled.length - 4);
                                var finaldate2 = trimmed + correctedyear;
                                // set the final date as the value and make the datepicker update it's location to this date'
                                $(this).val(finaldate2);
                            }
                            else {
                                var justformattheirdate = m4date.format(momentformat);
                                $(this).val(justformattheirdate);
                            }
                        }
                        $(this).datepicker('update');
                        if ($(this).parents('div#flightstable')) {
                            //Specific event for the booking screen flight modal datepickers
                            $(this).trigger('nowdodatepickerstuff');
                        }
                        $(this).valid();
                    }
                    else {
                        if ($(this).data("beenactive") === "Y") {
                            $(this).valid();
                        }
                    }
                }
            }
        }).on("focus", function() {
            if ($(this).val() !== "") {
                $(this).data("beenactive", "Y");
            }
        });
        if ($(this).val() === "" && $(this).is("[data-defaultdayadjustment]")) {
            var adjust = $(this).data("defaultdayadjustment");
            $(this).val(moment().add('days', adjust).format(momentformat));
        }
    });

    $('input.con-datepicker, input.con-startdate, input.con-enddate, input.con-futuredate, input.con-pastdate', target).attr('autocomplete', 'off');
    var datepickerdateformat = m4dateformat.moment;

    var twohundredyearsago = moment().subtract('years', 200).format(datepickerdateformat);
    var twohundredfromnow = moment().add('years', 200).format(datepickerdateformat);
    var defaultstartdatepickerdate = twohundredyearsago;
    var defaultenddatepickerdate = twohundredfromnow;

    $('.con-startdate', target).each(function() {
        if ($(this).data('date-limit') !== undefined) {
            defaultstartdatepickerdate = $(this).data('date-limit');
        }
        $(this).datepicker('setStartDate', defaultstartdatepickerdate);
    });
    $('.con-enddate', target).each(function() {
        if ($(this).data('date-limit') !== undefined) {
            defaultenddatepickerdate = $(this).data('date-limit');
        }
        $(this).datepicker('setEndDate', defaultenddatepickerdate);
    });

    // On blur of a startdate field, if it's paired enddate field doesn't yet have a value, then set it a week ahead from the selected startdate
    $('.con-startdate', target).on('blur', function() {
        // avoid changes when datepicker widget is open
        if ($('div.datepicker:visible').length > 0) {
            return;
        }
        var momentformat = "";
        if ($(this).data('moment-format') === undefined) {
            momentformat = m4dateformat.moment;
        }
        else {
            momentformat = $(this).data('moment-format');
        }
        var datepair = $(this).data("datepair");
        var adddays = $(this).data("adddays");
        var fromdateinput = $(this);
        var fromdate = fromdateinput.val();
        var todateinput = $("input[data-datepair=" + datepair + "]").not(".con-startdate");
        var todate = todateinput.val();
        var durationpairvalue = $(this).data('durationpair');
        var durationtarget = $("input[data-durationpair=" + durationpairvalue + "]").not(".con-enddate, .con-startdate");
        if (fromdate !== '') {
            var momentedfromdate = moment(fromdate, momentformat);
            if (durationtarget.length > 0) {
                var days = durationtarget.val();
                if (days !== '' && days > 0) {
                    todate = momentedfromdate.add('days', days).format(momentformat);
                    todateinput.val(todate);
                    todateinput.datepicker('update');
                    todateinput.addClass('pulse');
                    setTimeout(function() {
                        todateinput.removeClass('pulse');
                    }, 6000);
                } else if (todate !== '') {
                    var momentedtodate = moment(todate, momentformat);
                    days = momentedtodate.diff(momentedfromdate, 'days');
                    if (days > 0) {
                        durationtarget.val(days);
                        durationtarget.addClass('pulse');
                        setTimeout(function() {
                            durationtarget.removeClass('pulse');
                        }, 6000);
                    }
                }
                days = days === '' ? 0 : days;
                if (days <= 0) {
                    todate = '';
                }
            }
            if (todate === '') {
                var oneweekfromthen;
                if (typeof adddays === 'undefined') {
                    adddays = 7;
                }
                oneweekfromthen = momentedfromdate.add('days', adddays).format(momentformat);
                todateinput.val(oneweekfromthen);
                todateinput.datepicker('update');
                todateinput.addClass('pulse');
                setTimeout(function() {
                    todateinput.removeClass('pulse');
                }, 6000);
                if (durationtarget.length > 0) {
                    durationtarget.val(adddays);
                    durationtarget.addClass('pulse');
                    setTimeout(function() {
                        durationtarget.removeClass('pulse');
                    }, 6000);
                }
            }
        }
        $(this).valid();
    });

    $('.con-enddate', target).on('blur', function() {
        // avoid changes when datepicker widget is open
        if ($('div.datepicker:visible').length > 0) {
            return;
        }
        var momentformat = "";
        if ($(this).data('moment-format') === undefined) {
            momentformat = m4dateformat.moment;
        }
        else {
            momentformat = $(this).data('moment-format');
        }
        var datepair = $(this).data("datepair");
        var fromdateinput = $("input[data-datepair=" + datepair + "]").not(".con-enddate");
        var fromdate = fromdateinput.val();
        var todateinput = $(this);
        var todate = $(this).val();
        var durationpairvalue = $(this).data('durationpair');
        var durationtarget = $("input[data-durationpair=" + durationpairvalue + "]").not(".con-enddate, .con-startdate");
        if (durationtarget.length > 0 && todate !== '') {
            var momentedtodate = moment(todate, momentformat);
            var days = durationtarget.val();
            if (days !== '' && days > 0) {
                fromdate = momentedtodate.subtract('days', days).format(momentformat);
                fromdateinput.val(fromdate);
                fromdateinput.datepicker('update');
                fromdateinput.addClass('pulse');
                setTimeout(function() {
                    fromdateinput.removeClass('pulse');
                }, 6000);
            } else if (fromdate !== '') {
                var momentedfromdate = moment(fromdate, momentformat);
                days = momentedtodate.diff(momentedfromdate, 'days');
                if (days > 0) {
                    durationtarget.val(days);
                    durationtarget.addClass('pulse');
                    setTimeout(function() {
                        durationtarget.removeClass('pulse');
                    }, 6000);
                }
            }
        }
        $(this).valid();
    });

    $('input[data-durationpair]:not(".con-enddate, .con-startdate")', target).on('blur', function() {
        var durationpairvalue = $(this).data('durationpair');
        var fromdateinput = $("input[data-durationpair=" + durationpairvalue + "].con-startdate");
        var todateinput = $("input[data-durationpair=" + durationpairvalue + "].con-enddate");
        var momentformat = "";
        if ($(this).data('moment-format') === undefined) {
            momentformat = m4dateformat.moment;
        }
        else {
            momentformat = $(this).data('moment-format');
        }
        var days = $(this).val();
        var fromdate = fromdateinput.val();
        var todate = todateinput.val();
        if (fromdate !== '' && days !== '' && days > 0) {
            var momentedfromdate = moment(fromdate, momentformat);
            todate = momentedfromdate.add('days', days).format(momentformat);
            todateinput.val(todate);
            todateinput.datepicker('update');
            todateinput.addClass('pulse');
            setTimeout(function() {
                todateinput.removeClass('pulse');
            }, 6000);
        } else if (todate !== '' && days !== '' && days > 0) {
            var momentedtodate = moment(todate, momentformat);
            fromdate = momentedtodate.subtract('days', days).format(momentformat);
            fromdateinput.val(fromdate);
            fromdateinput.datepicker('update');
            fromdateinput.addClass('pulse');
            setTimeout(function() {
                fromdateinput.removeClass('pulse');
            }, 6000);
        }
    });

    $('span.add-on', target).has('i.icon-calendar').on('click', function() {
        var datepickerfield = $(this).parents('div.input-append').find('input.con-datepicker, input.con-startdate, input.con-enddate, input.con-futuredate, input.con-pastdate');
        datepickerfield.datepicker('show');
        datepickerfield.focus();
    });

}


/* $id$ */
/**
 * @class Drag-Drop Tables
 */

/**
 * Used to initialise drag drop functionality on all forms of draggable/sortable and droppable tables
 * @method draggableTableInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 * @return false On all buttons to prevent form submission by default
 */

// DRAGGABLE TABLES

function draggableTableInitialisation(target) {

    var fixHelper = function(e, ui) {
        ui.children().each(function() {
            $(this).width($(this).width());
            $(this).height($(this).height());
        });
        return ui;
    };

    //Initializes dragging on sortable table rows
    $('table.con-sortable tbody', target).sortable({
        handle: ".draghandle",
        helper: fixHelper
    });

    //Initializes droppable tbody
    $("table.con-droppable tbody", target).sortable({
        revert: true,
        helper: fixHelper,
        opacity: 0.5,
        handle: ".draghandle",
        // On drop, if it's a new row
        stop: function(event, ui) {
            if (ui.item.hasClass("newrow")) {
                var thisid = $(this).parents("table").attr("id");
                var i = $("#" + thisid + " tbody tr:visible").length;
                //Clone the hidden template row in the tbody
                $("#" + thisid + " tbody tr:first").clone().find(":input").each(function() {
                    // set the id and name to be unique
                    $(this).attr({
                        'id': function(_, id) {
                            return id + i;
                        },
                        'name': function(_, name) {
                            return name + i;
                        },
                        'value': ''
                    });
                    //and append the row to the tbody after the dropped row
                }).end().insertAfter(ui.item).addClass("float").removeClass("nodisplay").addClass("newrow").attr({
                    'id': function(_, id) {
                        return id + i;
                    }
                });
                var validationtarget = $("#" + thisid + " tr.newrow");
                // Clear dropped row
                ui.item.remove();
                // Run validation initialization over the new row.
                addvalidation(validationtarget);
                $("#" + thisid + " tr.newrow").find(" :input").each(function() {
                    $(this).bind("change", function() {
                        $(this).valid();

                    });
                });
                $("#" + thisid + " tr.newrow").removeClass("newrow");
                $("#" + thisid + " tr.placeholder").remove();
                adjustscroll(target);
            }
        }
    });

    // Initializes draggable rows and pairs them to their droppable tbody through the data-tablepair attribute
    $("table.con-draggable tr", target).each(function() {
        var tablepair = $(this).parents("table").data("tablepair");
        var pairedtable = $("table.con-droppable[data-tablepair=" + tablepair + "]").attr("id");
        var pairedtablebody = $("#" + pairedtable + " tbody");
        var options = {
            connectToSortable: pairedtablebody,
            helper: 'clone',
            placeholder: "ui-state-highlight",
            handle: ".draghandle",
            opacity: 0.5
        };
        $(this).draggable(options);
    });

    //  For droppable tables, if all actual rows are deleted, then produce a placeholder drop location row.
    $("table.con-droppable tbody button.deleterow", target).on("click", function() {
        var thisid = $(this).parents("table").attr("id");
        var i = $("#" + thisid + " tbody tr:visible").length;
        if (i < 2) {
            var colcount = $("#" + thisid + " thead th").length;
            $("#" + thisid + " tbody").append("<tr class='placeholder'><td colspan=" + colcount + "><p>Drop rows here</p></td></tr>");
        }
    });

}


/* $id$ */
/**
 * @class Email Modal
 */

/**
 * Initialises various buttons used in the email modals
 * @method emailModalInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */
function emailModalInitialisation(target) {

    $('.replyorforward', target).on('click', function() {
        $(this).parents('div.modal').find('input:disabled').attr('disabled', false);
        $(this).parents('div.modal').find('button[type=submit], div.fileupload-buttonbar button, div.fileinput-button').show();
        $(this).parents('div.modal').find('.fileupload').fileupload('enable');
    });

    $('.viewemailmodal', target).on('shown', function() {
        var cc = $(this).find('input[id$="cc"]');
        var bcc = $(this).find('input[id$="bcc"]');
        if (cc.attr('value') !== "" || cc.val() !== "") {
            cc.parents('div.control-group.nodisplay').show();
        }
        else {
            cc.parents('div.control-group.nodisplay').hide();
        }
        if (bcc.attr('value') !== "" || bcc.val() !== "") {
            bcc.parents('div.control-group.nodisplay').show();
        }
        else {
            bcc.parents('div.control-group.nodisplay').hide();
        }
        $(this).find('div.modal-body :input').not('div.fileupload-buttonbar button, div.fileinput-button').attr('disabled', "disabled");
        $(this).find('button[type=submit], div.fileupload-buttonbar button, div.fileinput-button').hide();
        $(this).find('.fileupload').fileupload('disable');
        disablerte($(this));
    });

    $('.emailstable tbody td:has("i.star")', target).on('click', function() {
        $(this).find('i.star').toggleClass('icon-star-empty');
    });

}


/* $id$ */
/**
 * @class Field Toggles
 */

/**
 * Initialises the various forms of visual toggling of elements .clickchangetrigger for radio controls, .checkboxcontrolled for checkboxes, .showhide for generic elements, .sectionbartoggle for section headerbar elements
 * @method fieldToggleInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @return   false on all functions here to prevent generic form submission
 * @version
 * @author Tom Yeldham
 */
function fieldToggleInitialisation(target) {

    // On load makes inputs paired to a, currently checked, checkbox to be visible
    $('div.controls label.radio input:checked, div.controls label.checkbox input:checked', target).parents('label').next(':input:hidden, div:has("input[data-search-url]"), div.styled-select').show();

    $('select.con-multiselect', target).each(function() {
        if ($(this).is('[data-clickchangegroup]:hidden') || $(this).is('[data-toggledby]:hidden')) {
            $(this).next().hide();
        }
        if ($(this).is(':visible')) {
            $(this).hide();
        }
    });

    //On load makes fields toggled by a selected option visible
    $('div.styled-select select option.clickchangetrigger:selected', target).each(function() {
        clickchangetrigger($(this));
    });

    $('select.onlyoneshow option.onlyonehide', target).on('click', function() {
        var parentid = $('select.onlyoneshow').attr('id');
        $('.' + parentid + 'hide').css("display", "none");
    });

    function clickchangetrigger(target) {
        var thisgroup = target.data('clickchangegroup');
        var thispair = target.data('clickchangepair');
        // Hides all non trigger elements in group of fields, then shows the one that pairs to the clicked element.
        $('[data-clickchangegroup=' + thisgroup + ']').not('.clickchangetrigger').hide(0);
        toggleevent($('[data-clickchangegroup=' + thisgroup + ']').not('.clickchangetrigger'));
        $('[data-clickchangegroup=' + thisgroup + ']').not('.clickchangetrigger').parents('div.controls').removeClass('error');
        $('[data-clickchangegroup=' + thisgroup + ']').not('.clickchangetrigger').removeClass('error');
        $('[data-clickchangegroup=' + thisgroup + ']').not('.clickchangetrigger').parents('div.controls').find('div.help-block').remove();
        $('[data-clickchangegroup=' + thisgroup + '][data-clickchangepair*=' + thispair + ']').show(0);
        toggleevent($('[data-clickchangegroup=' + thisgroup + '][data-clickchangepair*=' + thispair + ']'));
    }

    //  On change event for keyboard controls
    $('select:has("option.clickchangetrigger")', target).on('change', function(event) {
        clickchangetrigger($(this).find('option:selected'));
    });

    //  Same duplicated functionality for click events. Solves some weird bug related to clicking radios to my recolection
    $('input.clickchangetrigger', target).on('change click', function() {
        clickchangetrigger($(this));
    });

    // Used for checkboxes to toggle elements in and out. Clears all validation on hide.
    $('.checkboxcontrolled', target).on('change', function() {
        var targetinput = $('[data-toggledby="' + this.id + '"]');
        var targetinputid = targetinput.attr('id');
        if (targetinput.next().is($('div.help-block').has('[for="' + targetinputid + '"]'))) {
            targetinput.next().remove();
        }
        targetinput.removeClass('error');
        targetinput.toggle(0, function() {
            toggleevent(targetinput);
        });
        adjustscroll(target);
    });

    // Simple toggle function
    $('.showhide', target).on("click", function() {
        var objectstottoggle = $('.' + this.id);
        objectstottoggle.toggle(0, function() {
            toggleevent(objectstottoggle);
        });
        adjustscroll(target);
        if ($(this).parents('table#accomtable')) {
            $(this).find('i.icon-chevron-right, i.icon-chevron-down').toggleClass("icon-chevron-right").toggleClass("icon-chevron-down");
        }
    });

    // Sectionbar toggle function
    $('.sectionbartoggle', target).on("click", function() {
        $(this).siblings().toggle();
        $(this).find('i.icon-chevron-right, i.icon-chevron-down').toggleClass("icon-chevron-right").toggleClass("icon-chevron-down");
        adjustscroll(target);
        return false;
    });

    function toggleevent(toggledelement) {
        $.each(toggledelement, function() {
            if ($(this).is('select') && $(this).parent('div.styled-select').length > 0) {
                $(this).parent('div.styled-select').css('display', $(this).css('display'));
            }
            else {
                if ($(this).is('select.con-multiselect') === true) {
                    $(this).next('button.ui-multiselect').css('display', $(this).css('display'));
                    $(this).hide();
                }
            }
        });
        adjustscroll(target);
    }
}


/* $id$ */
/**
 * @class Helpers
 * @version
 * @author Tanvir
 * @author Sayeedul
 * @author Noor Siddique
 */

/**
 * Create an object having name => value pairs. Use to serialise form data.
 * Example: JSON.stringify($('#aform').serializeObject())
 * @author Tanvir
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * Helper function for posting JSON data over Ajax.
 * It expects JSON data in response.
 * @method $.postJSON
 * @param url Required parameter to send ajax post request
 * @param data Optional parameter. Data to send on post request.
 * @param callback Optional parameter. Success callback function.
 * @param error Optional parameter. Error callback function.
 * @author Tanvir
 */
$.postJSON = function(url, data, callback, error) {
    return jQuery.ajax({
        'type': 'POST',
        'url': url,
        'contentType': 'application/json',
        'data': JSON.stringify(data),
        'dataType': 'json',
        'success': callback,
        'error': error
    });
};

/**
 * Helper function for posting form-urlencoded data over Ajax.
 * It takes an Object as input and expects JSON data in response.
 * @method $.postObject
 * Examples:
 * a. Post an HTML Form by jQuery selector
 * $.postObject($('#mainform')[0].action, $('#mainform'));
 *
 * b. Post an HTML Form (DOM Element)
 * $.postObject($('#mainform')[0].action, $('#mainform')[0]);
 *
 * c. Post data from an Object
 * $.postObject($('#mainform')[0].action, { 'dccd': "TW", 'description': "Twin Bed" });
 *
 * d. Post using callbacks
 * $.postObject($('#mainform')[0].action, $('#mainform'),
 *         function(data, textStatus, jqXHR)  {
 *             console.log("success: " + JSON.stringify(data));
 *             // do something ...
 *         },
 *         function(jqXHR, textStatus, errorThrown) {
 *             console.log("error: " + textStatus + " ~ " + errorThrown + " ~ " + jqXHR.responseText);
 *             // do something ...
 *         }
 * );
 * @param url Required parameter to send ajax post request
 * @param data Optional parameter. Data to send on post request.
 * @param callback Optional parameter. Success callback function.
 * @param error Optional parameter. Error callback function.
 * @author Tanvir
 */
$.postObject = function(url, data, callback, error) {
    if (data instanceof HTMLFormElement) {
        data = $(data).serializeArray();
    } else if (typeof data.serialize == 'function') {
        data = data.serialize();
    }
    if (typeof callback == 'undefined') {
        callback = $.postObjectOnSuccess;
    }
    if (typeof error == 'undefined') {
        error = $.postObjectOnError;
    }
    return jQuery.ajax({
        'type': 'POST',
        'url': url,
        'data': data,
        'dataType': 'json',
        'success': callback,
        'error': error
    });
};

/**
 * Default $.postObject success callback function. If no success callback function
 * is provided in $.postObject call then following function will be executed.
 * It parses JSON response data and executes different type of operation depending
 * on data.
 * Examples:
 *     a. redirect: redirect indicates to redirect the page to a specific url which can be found as value.
 *     b. reloads: reload indicates to reload either whole page or sections of the page. {reload:{'div#section1':'/country/load','div#section2':'/resort/load'}}
 *     c. triggers: triggers indicates to trigger events on specified element. {trigger:{'div#section1':'onfinish','div#section2':'onfinish'}}
 *     d. status: status indicates the operation status can be success, error or info. Depending on the status it shows
 *         the message in main screen or modal depending of modal visibility 3 different type of alerts.
 *     e. errors: errors indicates validation errors. // TODO it can be replaced by status
 * @param data Ajax post function return parameter
 * @param textStatus Ajax post function return parameter
 * @param jqXHR Ajax post function return parameter
 * @param callback Optional parameter. It is a callback function to execute after all operations.
 * @author Tanvir
 * @author Sayeedul
 * @author Noor
 */
$.postObjectOnSuccess = function(data, textStatus, jqXHR, callback) {
    parseAndShowMessage(data);
    window.setTimeout(function() {
        var href = location.href.split('/');
        href = href[0] + '//' + href[2] + '/' + href[3];
        if (typeof data.redirect === 'string') {
            location.href = (data.redirect.indexOf('://') === -1 ? href : "") + data.redirect;
        } else {
            if (typeof data.reloads === 'object') {
                for (var key in data.reloads) {
                    var url = data.reloads[key];
                    if (url.indexOf('://') === -1)
                        url = href + url;
                    var elm = $('#' + key).parent();
                    elm.load(url + '#' + key, function() {
                        elm.trigger('reloaded');
                    });
                }
            } else {
                if (typeof data.triggers === 'object') {
                    for (var trkey in data.triggers) {
                        $(trkey).trigger(data.triggers[trkey]);
                    }
                }
            }
        }
        if ($.isFunction(callback))
            callback(data, textStatus, jqXHR);
    }, 1000);
};

/**
 * Default $.postObject error callback function.
 * @method $.postObjectOnError
 * @param jqXHR Ajax post function return parameter
 * @param textStatus Ajax post function return parameter
 * @param errorThrown Ajax post function return parameter
 * @param callback Optional parameter. It is a callback function to execute after all operations.
 * @author Tanvir
 * @author Sayeedul
 * @author Noor Siddique
 */
$.postObjectOnError = function(jqXHR, textStatus, errorThrown, callback) {
    showError(jqXHR.responseText);
    if ($.isFunction(callback)) {
        callback(jqXHR, textStatus, errorThrown);
    }
};


/**
 * Helper function for posting multipart/formdata data over Ajax.
 * It expects JSON data in response.
 *
 * Example:
 * $.postFormData($('#mainform')[0].action, new FormData($('#mainform')[0]),
 *         function(data, textStatus, jqXHR) {
 *             JSON.stringify(data);
 *         },
 *         function(jqXHR, textStatus, errorThrown) {
 *             console.log("error: " + textStatus + " ~ " + errorThrown);
 *         } );
 *
 */
$.postFormData = function(url, data, callback, error) {
    return jQuery.ajax({
        'type': 'POST',
        'url': url,
        'data': data,
        'dataType': 'json',
        'success': callback,
        'error': error
    });
};

/**
 * @method $.postObjectHTML
 * XXX we can use $.postObject instead of this
 */
$.postObjectHTML = function(url, data, callback, error) {
    if (data instanceof HTMLFormElement) {
        data = $(data).serializeArray();
    } else if (typeof data.serialize == 'function') {
        data = data.serialize();
    }
    if (typeof callback == 'undefined') {
        callback = function(data, textStatus, jqXHR) {
        };
    }
    if (typeof error == 'undefined') {
        error = $.postObjectOnError;
    }
    return jQuery.ajax({
        'type': 'POST',
        'url': url,
        'data': data,
        'dataType': 'html',
        'success': callback,
        'error': error
    });
};

/**
 * Debug event handlers attached via jQuery
 *
 * Example: printEvents('#flightconfirm')
 *
 * @param selector
 */
function printEvents(selector) {
    $(selector).each(function() {
        $.each($._data(this, 'events'), function() {
            $.each(this, function() {
            });
        });
    });
}

var numberOfClicks= [];

$('body').on('click','.logo',function(e){
    if(numberOfClicks.length < 10){
       numberOfClicks.push(new Date().getTime());
    }else{
        var diff = numberOfClicks[numberOfClicks.length -1] - numberOfClicks[0];
        console.log(diff);
        if(diff < 5000){
            //alert("toomany!");
            $('.logo').toggleClass('autocompletesearching');
            numberOfClicks = [];
            e.preventDefault();
        }
        numberOfClicks.shift();
        numberOfClicks.push(new Date().getTime());
        console.log(numberOfClicks);
    }
});



/* $id$ */
/**
 * @class In Row Edit
 */

/**
 * Initialises buttons and keyboard nav on in grid tables
 * @method inrowEditTableInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @param {object} submitSetting Required global variable from screen specific searchconfig.js. Contains a series of sets of parameters, each with a unique key. Each input is paired to the desired set of parameters by attaching a submit-settings data attribute to it containing the key of the set that is required. These settings are passed to the generic inroweditsubmit function. See examples below.
 * @version
 * @author Tom Yeldham
 */
function inrowEditTableInitialisation(target) {

    if (typeof(localStorage) !== 'undefined' ) {
        $.each($('table.gridtable', target), function() {
            $(this).find(':input').unbind();
            var id = $(this).attr('id');
            var markup = $(this).parent().html();
            localStorage.setItem(id, markup);
            setuptablenav($(this));
        });

        // Initialise Keyboard control for tablecells. Allows keyboard navigation around the cells if set up correctl

        $('.gridtableeditbutton', target).on('click', function() {
            $(this).parents('table').find(':disabled').removeAttr('disabled');
            $(this).parents('table').find('.gridtablenewbutton, .gridtableconfirmchangesbutton, .gridtablecancelchangesbutton').show();
            $(this).hide();
            return false;
        });

        $('.gridtablenewbutton', target).on('click', function() {
            var tbody = $(this).parents('table').find('tbody');
            tbody.find('tr:last-child').clone().addClass('cloneofrow').appendTo(tbody);
            var newrow = tbody.find('.cloneofrow');
            var newrowinputs = newrow.find(':input');
            reidrowinputs($(this).parents('form'));
            newrow.find('.error').removeClass('error');
            newrow.find('div.help-block').remove();
            metaboot(newrow);
            editmode(newrow);
            setuptablenav(newrow);
            $.each(newrowinputs, function() {
                $(this).val("");
                $(this).removeClass('error');
            });
            newrow.removeClass('cloneofrow');
            var thisid = $(this).parents("table").attr("id");
            var i = $("#" + thisid + " tbody tr:visible").length;
            if (i >= 2) {
                $("table.gridtable tbody button.deleterow").show();
            }
            $(this).parents('table').trigger('cleanandvalidate');
            return false;
        });

        $('.gridtableconfirmchangesbutton', target).on('click', function() {
            if ($(this).parents('form').valid()) {
                var submitsettings = $(this).data('submit-settings');
                var submitconfig = submitSetting[submitsettings];
                if (submitsettings === undefined || submitsettings === "" || submitconfig === undefined) {
                    submitconfig = $(this).attributesAsArray("data-submit-");
                }
                submitconfig.form = $(this).parents('form');
                $(this).trigger("rowsubmitinitialise", submitconfig);
                inroweditsubmit(submitconfig);
            }
        });

        $('.gridtablecancelchangesbutton', target).on('click', function() {
            var parentform = $(this).parents('table.gridtable').parent();
            var parenttableid = $(this).parents('table.gridtable').attr('id');

            // remove the rows in the specified table before we start
            parentform.empty();
            // get the table from local storage using the id of the table as the key, append it to the table and then initialise it
            var tablemarkup = localStorage.getItem(localStorage.key(findIndexOfKey(parenttableid)));
            parentform.append(tablemarkup);
            inrowEditTableInitialisation(parentform);
            metaboot(parentform);
            editmode(parentform);
            addvalidation(parentform);


            // either use an ajax call to refresh or maybe get it from local storage
            //            $(this).parents('table').find('tbody :input').attr('disabled', 'disabled');
            //            $(this).parents('table').find('.gridtablenewbutton, .gridtableconfirmchangesbutton, .gridtablecancelchangesbutton').hide();
            //            $(this).parents('table').find('.gridtablenewbutton').show()
        });

        $('table.gridtable', target).on('cleanandvalidate', function() {
            $.each($(this).find(':input').not('button'), function() {
                $(this).rules("remove");
                $(this).siblings('div.help-block').not('div.help-block:has(span[for="' + $(this).attr('id') + '"])').find('span').attr('for', $(this).attr('id'));
            });
            $(this).find('td div.help-block:empty').remove();
            addvalidation($(this));
        });
    }
}


/* $id$ */
/**
 * @class Inputs
 */

/**
 * Initialises general functionality on various form elements
 * @method inputInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */
function inputInitialisation(target) {

    /**
     * Generalised tooltip intialisation function. Used primarily for input add-ons on elements that have helper tooltips, ie datepickers
     * @method initialiseaddontooltips
     * @param {Jquery object} targetelement Required parameter used to target the initialisation on the desired element.
     * @param {string} message Required parameter. Sets the tooltip message for that addon.
     * @version
     * @author Tom Yeldham
     */
    function initialiseaddontooltips(targetelement, message) {
        var parentelement = targetelement.parents('div.control-group, td, th, span');
        targetelement.tooltip({
            title: message,
            animation: true,
            trigger: 'hover',
            delay: {
                show: 600,
                hide: 250
            },
            placement: function(tip, element) {
                var $element, above, actualHeight, actualWidth, below, boundBottom, boundLeft, boundRight, boundTop, elementAbove, elementBelow, elementLeft, elementRight, isWithinBounds, left, pos, right;
                isWithinBounds = function(elementPosition) {
                    return boundTop < elementPosition.top && boundLeft < elementPosition.left && boundRight > (elementPosition.left + actualWidth) && boundBottom > (elementPosition.top + actualHeight);
                };
                $element = $(element);
                pos = $.extend({}, $element.offset(), {
                    width: element.offsetWidth,
                    height: element.offsetHeight
                });
                actualWidth = 283;
                actualHeight = 117;
                boundTop = $(document).scrollTop();
                boundLeft = $(document).scrollLeft();
                boundRight = boundLeft + $(window).width();
                boundBottom = boundTop + $(window).height();
                elementAbove = {
                    top: pos.top - actualHeight,
                    left: pos.left + pos.width / 2 - actualWidth / 2
                };
                elementBelow = {
                    top: pos.top + pos.height,
                    left: pos.left + pos.width / 2 - actualWidth / 2
                };
                elementLeft = {
                    top: pos.top + pos.height / 2 - actualHeight / 2,
                    left: pos.left - actualWidth
                };
                elementRight = {
                    top: pos.top + pos.height / 2 - actualHeight / 2,
                    left: pos.left + pos.width
                };
                above = isWithinBounds(elementAbove);
                below = isWithinBounds(elementBelow);
                left = isWithinBounds(elementLeft);
                right = isWithinBounds(elementRight);
                if (above) {
                    return "top";
                } else {
                    if (below) {
                        return "bottom";
                    } else {
                        if (left) {
                            return "left";
                        } else {
                            if (right) {
                                return "right";
                            } else {
                                return "right";
                            }
                        }
                    }
                }
            },
            container: parentelement
        }).on('show', function(e) {
            e.stopPropagation();
        }).on('hidden', function(e) {
            e.stopPropagation();
        });
    }

    $(":input[data-valid='postcode']", target).on('keypress', function() {
        $(this).val($(this).val().toUpperCase());
    });

// Initializes tooltips
    $('[rel=tooltip] ,[data-toggle=tooltip]', target).each(function() {
        initialiseaddontooltips($(this));
    });
    $("input[data-valid='distance'], input[data-valid='weight'], input[data-valid='dims'], input[data-valid='currencytogbp']", target).each(function() {
        var type = $(this).data('valid');
        var message = "";
        switch (type) {
            case "distance":
                message = "For miles -> km conversion, just type mi after the distance and it will be converted for you.";
                break;
            case "weight" :
                message = "For pound -> kilo conversion, just type lb after the weight and it will be converted for you. Hover here to see the weight in lbs.";
                break;
            case "dims" :
                message = "For inch -> cm conversion, just type in after the dimension and it will be converted for you. Hover here to see the dimension in inches.";
                break;
            case "currencytogbp" :
                message = "For conversion to GBP, just type gpb after the currency and it will be converted for you. Hover here to see the amount in the alternative currency.";
                break;
        }
        initialiseaddontooltips($(this).parents('div.input-append').find('span.add-on'), message);
    });
    $('span.add-on', target).has('i.icon-calendar').each(function() {
        initialiseaddontooltips($(this), 'Click here or type "?" into the field to open the datepicker. Alternatively type your date into the field');
        $(this).addClass('clickableaddon');
    });
    $('input.con-dobfield', target).parents('div.input-append').find('span.add-on').each(function() {
        initialiseaddontooltips($(this), 'D.o.B fields require a full DD-MM-YYYY formatted date');
    });
    $('span.add-on', target).has('i.icon-time').each(function() {
        initialiseaddontooltips($(this), 'Time fields accept 3 or 4 digit strings. Delimiters will be added if not provided');
    });
    $('span.add-on', target).has('i.icon-search').each(function() {
        initialiseaddontooltips($(this), 'Just type into the field to start searching for results');
    });
    $('.con-dobfield', target).on('blur', function() {
        if ($(this).valid() && $(this).val().length > 0) {
            var digits = $(this).val().replace(/[\.,-\/#!$ï¿½%\^& \*;:{}=\-_`~()a-zA-Z]/g, '');
            var days = digits.substring(0, 2);
            var months = digits.substring(2, 4);
            var years = digits.substring(4, 8);
            $(this).val(days + "-" + months + "-" + years);
        }
    });

    $('.con-timepicker, .con-starttime, .con-endtime', target).on('blur', function() {
        var typedtime = $(this).val();
        var typedigits = typedtime.replace(/[\.,-\/#!$ï¿½%\^& \*;:{}=\-_`~()a-zA-Z]/g, '');
        var noofdigits = typedigits.length;
        if (typedtime.length > 0) {
            if (noofdigits > 0 && typedtime.replace(/[\.,-\/#!$ï¿½%\^& \*;:{}=\-_`~()0-9]/g, '').length === 0) {
                if (noofdigits === 1) {
                    typedigits = "0" + typedigits + "00";
                }
                if (noofdigits === 2) {
                    typedigits = typedigits + "00";
                }
                if (typedigits.length === 3) {
                    typedigits = "0" + typedigits;
                }

                var hours = typedigits.substring(0, 2);
                var mins = typedigits.substring(2, 4);
                if ((hours === "00" && mins === "00") || (hours <= "23" && mins <= "59")) {
                    $(this).val(hours + ":" + mins);
                }
                else {
                    $(this).val(typedtime);
                }
            }
            $(this).valid();
        }
    });

    $("input:text", target).focus(function() {
        $(this).select();
    });
    // Pairs 2 fields to have the same value as one another. Not complete
    $('[data-valuepair]', target).on('change', function() {
        var pair = $(this).data('valuepair');
        var value = $(this).val();
        $('[data-valuepair=' + pair + ']').val(value).valid();
    });
    //  Stops text-ares from being able to be made smaller than their initialized width.
    $('textarea', target).each(function() {
        var width = $(this).width();
        $(this).css('min-width', width);
    });
    // Initializes popovers
    $('[rel=popover], [data-toggle=popover]', target).each(function() {
        var parentelement = $($(this).parents('div.control-group, td, th, span')[0]);
        $(this).popover({
            animation: true,
            placement: function(tip, element) {
                var $element, above, actualHeight, actualWidth, below, boundBottom, boundLeft, boundRight, boundTop, elementAbove, elementBelow, elementLeft, elementRight, isWithinBounds, left, pos, right;
                isWithinBounds = function(elementPosition) {
                    return boundTop < elementPosition.top && boundLeft < elementPosition.left && boundRight > (elementPosition.left + actualWidth) && boundBottom > (elementPosition.top + actualHeight);
                };
                $element = $(element);
                pos = $.extend({}, $element.offset(), {
                    width: element.offsetWidth,
                    height: element.offsetHeight
                });
                actualWidth = 283;
                actualHeight = 117;
                boundTop = $(document).scrollTop();
                boundLeft = $(document).scrollLeft();
                boundRight = boundLeft + $(window).width();
                boundBottom = boundTop + $(window).height();
                elementAbove = {
                    top: pos.top - actualHeight,
                    left: pos.left + pos.width / 2 - actualWidth / 2
                };
                elementBelow = {
                    top: pos.top + pos.height,
                    left: pos.left + pos.width / 2 - actualWidth / 2
                };
                elementLeft = {
                    top: pos.top + pos.height / 2 - actualHeight / 2,
                    left: pos.left - actualWidth
                };
                elementRight = {
                    top: pos.top + pos.height / 2 - actualHeight / 2,
                    left: pos.left + pos.width
                };
                above = isWithinBounds(elementAbove);
                below = isWithinBounds(elementBelow);
                left = isWithinBounds(elementLeft);
                right = isWithinBounds(elementRight);
                if (above) {
                    return "top";
                } else {
                    if (below) {
                        return "bottom";
                    } else {
                        if (left) {
                            return "left";
                        } else {
                            if (right) {
                                return "right";
                            } else {
                                return "right";
                            }
                        }
                    }
                }
            },
            trigger: 'hover',
            delay: {
                show: 600,
                hide: 250
            },
            html: true,
            container: parentelement
        }).on('show', function(e) {
            e.stopPropagation();
        }).on('hidden', function(e) {
            e.stopPropagation();
        });
    });
    $('[data-toggle=autowidthpopover]', target).each(function() {
        var parentelement = $($(this).parents('div.control-group, td, th, span')[0]);
        $(this).popover({
            animation: true,
            placement: function(tip, element) {
                var $element, above, actualHeight, actualWidth, below, boundBottom, boundLeft, boundRight, boundTop, elementAbove, elementBelow, elementLeft, elementRight, isWithinBounds, left, pos, right;
                isWithinBounds = function(elementPosition) {
                    return boundTop < elementPosition.top && boundLeft < elementPosition.left && boundRight > (elementPosition.left + actualWidth) && boundBottom > (elementPosition.top + actualHeight);
                };
                $element = $(element);
                pos = $.extend({}, $element.offset(), {
                    width: element.offsetWidth,
                    height: element.offsetHeight
                });
                actualWidth = isNaN($element.data("contentwidth")) ? 283 : parseInt($element.data("contentwidth"));
                actualHeight = isNaN($element.data("contentheight")) ? 117 : parseInt($element.data("contentheight"));
                boundTop = $(document).scrollTop();
                boundLeft = $(document).scrollLeft();
                boundRight = boundLeft + $(window).width();
                boundBottom = boundTop + $(window).height();
                elementAbove = {
                    top: pos.top - actualHeight,
                    left: pos.left + pos.width / 2 - actualWidth / 2
                };
                elementBelow = {
                    top: pos.top + pos.height,
                    left: pos.left + pos.width / 2 - actualWidth / 2
                };
                elementLeft = {
                    top: pos.top + pos.height / 2 - actualHeight / 2,
                    left: pos.left - actualWidth
                };
                elementRight = {
                    top: pos.top + pos.height / 2 - actualHeight / 2,
                    left: pos.left + pos.width
                };
                above = isWithinBounds(elementAbove);
                below = isWithinBounds(elementBelow);
                left = isWithinBounds(elementLeft);
                right = isWithinBounds(elementRight);
                if (above) {
                    return "top";
                } else if (below) {
                    return "bottom";
                } else if (left) {
                    return "left";
                } else {
                    return "right";
                }
            },
            trigger: 'hover',
            delay: {
                show: 600,
                hide: 250
            },
            html: true,
            container: parentelement,
            template: '<div class="popover in autowidth"><div class="arrow"></div><div class="popover-inner"><h3 class="popover-title"></h3><div class="popover-content"><p></p></div></div></div>'
        }).on('show', function(e) {
            e.stopPropagation();
        }).on('hidden', function(e) {
            e.stopPropagation();
        });
    });

    initstyledselects(target);

    $('div.styled-select', target).on('show', 'select', function() {
        $(this).parents('div.styled-select').show();
    });
    $('div.styled-select', target).on('hide', 'select', function() {
        $(this).parents('div.styled-select').hide();
    });
    $('div.styled-select', target).on('toggle', 'select', function() {
        $(this).parents('div.styled-select').toggle();
    });
    $('div.styled-select', target).on('focus', 'select', function() {
        $(this).parents('div.styled-select').addClass('focussed');
    });
    $('div.styled-select', target).on('blur', 'select', function() {
        $(this).parents('div.styled-select').removeClass('focussed');
    });

    $(':input:not(textarea)').on("keydown", function(event) {
        if (event.which === 13) {
            event.preventDefault();
        }
    });
}



/* $id$ */
/**
 * @class Messages
 * @version
 * @author Noor Siddique
 */

/**
 * Parses JSON data and shows messages (either in modal or in screen) depending on type or modal visibility
 * @method parseAndShowMessage
 * @param {JSON Array} data json data mapping (data.status, data.message)
 * @param {String} type Optional parameter (either 'screen' or 'modal') to specify where to show the message. If not provided it will show message either in modal if modal is visible or in screen
 * @version
 * @author Noor Siddique
 */
function parseAndShowMessage(data, type) {
    if (data.message === undefined || data.message === "" ||
            data.status === undefined || data.status === "") {
        return;
    }
    var cssclass = "";
    switch (data.status) {
        case "success":
            cssclass = "alert-success";
            break;
        case "info":
            cssclass = "alert-info";
            break;
        default:
            cssclass = "alert-error";
    }
    showMessage(cssclass, data.message, type);
}

/**
 * Shows success message either in modal or in screen depending on type or modal visibility if type not provided
 * @method showSuccess
 * @param {String} message Required parameter. The text to display.
 * @param {String} type Optional parameter (either 'screen' or 'modal') to specify where to show the message.
 * If not provided it will show message either in modal if modal is visible or in screen
 * @version
 * @author Noor Siddique
 */
function showSuccess(message, type) {
    showMessage("alert-success", message, type);
}

/**
 * Shows info message either in modal or in screen depending on type or modal visibility if type not provided
 * @method showInfo
 * @param {String} message Required parameter. The text to display.
 * @param {String} type Optional parameter (either 'screen' or 'modal') to specify where to show the message.
 * If not provided it will show message either in modal if modal is visible or in screen
 * @version
 * @author Noor Siddique
 */
function showInfo(message, type) {
    showMessage("alert-info", message, type);
}

/**
 * Shows error message either in modal or in screen depending on type or modal visibility if type not provided
 * @method showError
 * @param {String} message Required parameter. The text to display.
 * @param {String} type Optional parameter (either 'screen' or 'modal') to specify where to show the message.
 * If not provided it will show message either in modal if modal is visible or in screen
 * @version
 * @author Noor Siddique
 */
function showError(message, type) {
    showMessage("alert-error", message, type);
}

/**
 * Shows message in modal or screen depending on type or modal visibility if type is not provided.
 * @method showMessage
 * @param {String} alertClass Required parameter to specify the alert type. Three possible classes are alert-success, alert-info and alert-error
 * @param {String} message Required parameter. The text to display.
 * @param {String} type Optional parameter (either 'screen' or 'modal') to specify where to show the message.
 * @version
 * @author Noor Siddique
 */
function showMessage(alertClass, message, type) {
    if ((type === undefined && $("div.modal-header").is(":visible")) || (type !== undefined && type !== "screen")) {
        showModalMessage(alertClass, message);
    } else {
        showScreenMessage(alertClass, message);
    }
}

/**
 * Shows message in modal.
 * @method showModalMessage
 * @param {String} alertClass Required parameter to specify the alert type. Three possible classes are alert-success, alert-info and alert-error
 * @param {String} message Required parameter. The text to display.
 * @version
 * @author Noor Siddique
 */
function showModalMessage(alertClass, message) {
    var elementAfter = $("div.modal-header:visible");
    var alerthtml = "<div class='alert fade in nodisplay " + alertClass + "' style='margin-bottom: 0px'><button type='button' class='alertremoval close' data-dismiss='alert'>&times;</button><span></span></div>";
    elementAfter.siblings(".alert").remove();
    $(alerthtml).insertAfter(elementAfter);
    var alertdiv = elementAfter.siblings("." + alertClass);
    alertdiv.show(500);
    alertdiv.children("span").text(message);
    alertremoval(alertdiv);
    alertMessageCloseTimeout(alertdiv);
}

/**
 * Shows message in screen.
 * @method showScreenMessage
 * @param {String} alertClass Required parameter to specify the alert type. Three possible classes are alert-success, alert-info and alert-error
 * @param {String} message Required parameter. The text to display.
 * @version
 * @author Noor Siddique
 */
function showScreenMessage(alertClass, message, triggerelement) {
    $('.headeralert').remove();
    $("header#overview").append("<div class='headeralert alert fade in nodisplay subnav-fixed " + alertClass + "'><button type='button' class='alertremoval close preventmessages' data-dismiss='alert'>&times;</button><span>" + message + "</span></div>");
    headeralertfix();
    $('.headeralert').find('.preventmessages').on('click', function () {
        if (triggerelement) {
            triggerelement.data('preventalert', 'Y');
        }
    });
    $('.headeralert').show(500);
    alertremoval($('.headeralert'));
    // Set timer to remove banner
    alertMessageCloseTimeout($('.headeralert'));
}

/**
 * Sets timeout function to close alert div from showing
 * @method alertMessageCloseTimeout
 * @param {Jquery object} element Required parameter to set time out function to close the alert after a certain time (20 seconds)
 * @version
 * @author Noor Siddique 
 */
function alertMessageCloseTimeout(element) {
    window.setTimeout(function () {
        element.remove();
    }, 20000);
}

$('[data-triggeralert]').on('blur', function () {
    if (!$(this).data('preventalert')) {
        showScreenMessage('alert-info', $(this).data('alertmessage'), $(this));
    }
    console.log('alertprevented');
});


/* $id$ */
/**
 * @class Metaboot
 */

function metaboot(target) {
    inrowEditTableInitialisation(target);
    viewEditModeEventHandlingInitialisation(target);
    multiselectInitialisation(target);
    datepickerInitialisation(target);
    inputInitialisation(target);
    fieldToggleInitialisation(target);
    pageInitialisation(target);
    navInitialisation(target);
    oldInroweditTableInitialisation(target);
    draggableTableInitialisation(target);
    searchSuggestInitialisation(target);
    tableInitialisation(target);
    uploaderInitialisation(target);
    rteInitialisation(target);
    modalsInitialisation(target);
    emailModalInitialisation(target);

    adjustscroll(target);
}


/* $id$ */
/**
 * @class Modals
 */

/**
 * All the modal Initialisation code
 * @method modals
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */
function modalsInitialisation(target) {

    //Locks modal scroll in place when a modal is fired from within another modal
    $('div.modal:not(".bootstrap-wysihtml5-insert-link-modal")', target).on('shown', function() {
        $('body').css('overflow', 'hidden');
        initstyledselects($('div.modal:visible'));
    }).on('hidden', function(e) {
        $('body').css('overflow', 'auto');
    });

    // Clears remotely loaded content on modal close
    $('div.modal:not(".bootstrap-wysihtml5-insert-link-modal")', target).on('hidden', function() {
        $(this).removeData('modal');
    });





    /**
     * Sets adjustmodalbodyminheight event listener on all modals and the triggers for it
     * @method modalHeightAdjustmentInitialisation
     * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
     * @version
     * @author Tom Yeldham
     */

    //MODAL HEIGHT ADJUSTMENT

    function modalHeightAdjustmentInitialisation(target) {

        // On modal show fire custom event
        $('div.modal:not(".bootstrap-wysihtml5-insert-link-modal")', target).on('shown', function() {
            $(this).find('.modal-body').trigger('adjustmodalbodyminheight');
        });

        // Which causes the modal to calculate the visible space on the screen and adjust itself vertically to fill it correctly
        $('div.modal-body', target).on('adjustmodalbodyminheight', function() {
            var headerheight = $(this).parents('div.modal').find('div.modal-header:visible').height();
            var footerheight = $(this).parents('div.modal').find('div.modal-footer:visible').height();
            var viewportHeight = $(window).height();
            var combinedheights = viewportHeight - (headerheight + footerheight) - 120;
            var negativetop = (viewportHeight / 2) - 20;
            $(this).parents('div.modal').css('margin-top', '-' + negativetop + 'px');
            $(this).css('max-height', '' + combinedheights + 'px');
        });


    }



    /**
     * Initialises various functionality for all modal dismiss buttons.
     * @method modalCloseInitialisation
     * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
     * @version
     * @author Tom Yeldham
     */

    //MODAL CLOSE

    function modalCloseInitialisation(target) {

        $('div.modal', target).on('change', ':input', function() {
            var modalclosebutton = $(this).parents('div.modal').find('div.modal-footer button.modalclose');
            if (modalclosebutton.data('firewarning') !== "Y") {
                modalclosebutton.data('firewarning', 'Y');
            }
        });

        $('div.modal', target).on('click', 'button.modalclose', function() {
            if ($(this).data('firewarning') === "Y") {
                var r = confirm("Close without saving?");
                if (r === true)
                {
                    $(this).parents('div.modal').modal('hide');
                    $(this).parents('div.modal').find('tr.template-download, .fileuploaderlog, #modalsubmitlog').remove();
                }
            }
            else {
                $(this).parents('div.modal').modal('hide');
            }
        });

        $('div.modal:not(".bootstrap-wysihtml5-insert-link-modal")', target).on('shown', function() {
            $(this).find('div.modal-footer button.modalclose').data('firewarning', "");
        });

        // Selects modal cancel buttons in any modals with an id containing add or new, resets the contents of the modal
        $('div.modal[id*="add"], div.modal[id*="new"]', target).on('shown', function() {
            $(this).parents('div.modal').find('input:text, textarea, input[type="date"], input[type="password"], select').val('');
            $(this).find(':input:checked').attr('checked', false);
            $(this).find('[type="radio"]').each(function() {
                $(this).find('[type="radio"][name="' + $(this).attr('name') + '"]').first().prop("checked", true);
            });
            $(this).parents('div.modal').find('.nodisplay').hide();
            $(this).parents('div.modal').find('div.error').removeClass('error');
            $(this).parents('div.modal').find('div.help-block').remove();
        });

    }





    /**
     * Initialises functionality on modal submit buttons. Like search suggests accepts a global parameter in the form of submit settings, which are set up in the same js as the search suggest settings.
     * @method modalSubmitInitialisation
     * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
     * @param {object} submitSetting Required global variable from screen specific searchconfig.js. Contains a series of sets of parameters, each with a unique key. Each input is paired to the desired set of parameters by attaching a submit-settings data attribute to it containing the key of the set that is required. See examples below.
     * @version
     * @author Tom Yeldham
     */


    //MODAL SUBMIT

    function modalSubmitInitialisation(target) {
        $('div.modal-footer button[data-submit-settings]', target).off();
        // Make non booking modal modals reset themselves when closed
        $('div.modal:not("div.bookingmodal, .bootstrap-wysihtml5-insert-link-modal")', target).on('hidden', function() {
            $(this).find('input:text, textarea, input[type="date"], input[type="password"], select').val('');
            $(this).find('.error').removeClass('error');
            $(this).find('div.help-block').remove();
            $(this).find(':input:checked').attr('checked', false);
            $(this).find('[type="radio"]').each(function() {
                $(this).find('[type="radio"][name="' + $(this).attr('name') + '"]').first().prop("checked", true);
            });
            $(this).find("select").val([]);
            $(this).find('ul.qq-upload-list li').remove();
            $(this).find('div.alert-error').remove();
        });

        $('div.modal-footer button[data-submit-settings]', target).on('click', function(event) {
            event.preventDefault();
            var submitbutton = $(this);
            var submitsettings = submitbutton.data('submit-settings');
            var submitconfig = submitSetting[submitsettings];
            if (submitsettings === "" || submitconfig === undefined) {
                submitconfig = submitbutton.attributesAsArray("data-submit-");
            }
            var ev = $.Event("modalsubmitinitialise");
            submitbutton.trigger(ev, submitconfig);
            if (ev.isDefaultPrevented())
                return;
            if (submitbutton.parents('div.modal').find('div.roombox:visible').length > 0) {
                var associatedform = submitbutton.attr('form');
                if ($('form#' + associatedform + '').valid()) {
                    if (submitbutton.parents('div.modal').find('div.roomheader.selected').length > 0) {
                        // TODO Submit code to be added by Dhaka
                        // TODO need to discuss how to generalize the scope and modify following code [Noor 131113]
                        modalsubmit(submitconfig);
                    }
                    else {
                        if (submitbutton.parents('div.modal-footer').find('div.roomerrorbox').length === 0) {
                            submitbutton.parents('div.modal-footer').append('<div class="alert alert-info roomerrorbox"><strong>Uh oh...</strong>Please price and select at least 1 item</div>');
                        }
                    }
                }
            }
            else {
                submitconfig.invalid = getExecutableFunction(submitconfig.invalid);
                if (submitbutton.parents('form').length > 0 || submitbutton.attr('form') !== "") {
                    var associatedform3 = submitbutton.parents('form');
                    if (associatedform3.length === 0) {
                        associatedform3 = $('form#' + submitbutton.attr('form') + '');
                    }
                    if (submitconfig.url === undefined) {
                        submitconfig.url = associatedform3.attr("action");
                    }
                    if (submitconfig.type === undefined) {
                        submitconfig.type = associatedform3.attr("method");
                    }
                    if (submitconfig.data === undefined) {
                        submitconfig.data = associatedform3.serialize();
                    }

                    if (submitbutton.is('.dontvalidate')) {
                        modalsubmit(submitconfig);
                    } else {
                        if (associatedform3.valid()) {
                            modalsubmit(submitconfig);
                        } else {
                            submitconfig.invalid();
                        }
                    }
                } else {
                    // TODO need to understand the scope and modify following code [Noor 131113]
                    var associatedform2 = submitbutton.parents('div.modal').find('form:visible');
                    if (submitbutton.is('.dontvalidate')) {
                        modalsubmit(submitconfig);
                    } else {
                        var invalid = 0;
                        $(associatedform2).each(function() {
                            if ($(this).valid() === false) {
                                invalid++;
                            }
                        });
                        if (invalid === 0) {
                            modalsubmit(submitconfig);
                        } else {
                            submitconfig.invalid();
                        }
                    }
                }
            }
        });

    }

    modalHeightAdjustmentInitialisation(target);
    modalCloseInitialisation(target);
    modalSubmitInitialisation(target);
}


/* $id$ */
/**
 * @class Multi-select
 */

/**
 * Initialises multiselect widget on any inputs with class of con-multiselect
 * @method multiselectInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */
function multiselectInitialisation(target) {

    $(window).on("resize", function() {
        $("select.con-multiselect").multiselect("position");
    });

    // Initializes multiselects
    $('select.con-multiselect', target).each(function() {
        var options = {
            selectedList: 1,
            selectedText: function(numChecked, numTotal, checkedItems) {
                var selectedList = 10;
                if ($(this).data('displayno')) {
                    selectedList = $(this).data('displayno');
                }
                if (checkedItems.length > selectedList)
                {
                    return checkedItems.length + " of " + numTotal + " selected";
                } else
                {
                    var selectedTextArr = [];
                    for (var i = 0; i < checkedItems.length; i++)
                    {
                        selectedTextArr.push($(checkedItems[i]).siblings().text());
                    }
                    return selectedTextArr.join(', ').replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<");
                }
            },
            noneselectedtext: "Select as many as required",
            beforeopen: function() {
                if ($('div.modal:visible').length > 0) {
                    var multiselectpopup = $(this).multiselect("widget");
                    multiselectpopup.css('z-index', '1060');
                }
            },
            position: {
                my: 'left top',
                at: 'left bottom',
                collision: "flip flip"
            }
        };
        if ($(this).parents('div.modal').length > 0) {
            options.appendTo = '#' + $(this).parents('div.modal').attr('id');
        }
        if ($(this).is('[data-limit], .combinablemultiselect')) {
            options.uncheckAllText = "";
            options.checkAllText = "";
        }
        $(this).multiselect(options).multiselectfilter();
    });

    $('select.con-multiselect[data-limit]', target).on('change', function(event, ui) {
        var thisid = $(this).attr('id');
        var replacementoptions = $('ul.ui-multiselect-checkboxes li label input[name*="' + thisid + '"]');
        var currentlyselected = replacementoptions.filter('[aria-selected="true"]');
        var currentlyselectedno = currentlyselected.length;
        var maxselectable = $(this).data('limit');
        if (maxselectable !== "") {
            if (currentlyselectedno >= maxselectable) {
                replacementoptions.not(currentlyselected).attr('disabled', 'disabled');
            } else {
                replacementoptions.removeAttr('disabled');
            }
        }
    });

}


/* $id$ */
/**
 * @class Navigation
 */

/**
 * Initialises keyboard navigation for all fields and pageheaders
 * @method navInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */

function navInitialisation(target) {

    // Keyboard navigation controls from fields
    $(':input', target).on("keydown", function(event) {
        if (event.ctrlKey) {
            if ($('div.datepicker:visible').length < 1) {
                var offsetheight = -($("div.subnav").height() + 40);

                if (event.which === 38) {
                    $.scrollTo($(this).parents('section').find('div.page-header'), 1200, {
                        offset: offsetheight
                    });
                    $(this).parents('section').find('div.page-header').focus();
                }
                else if (event.which === 40) {
                    if ($(this).parents('section').next('section').length > 0) {
                        $.scrollTo($(this).parents('section').next('section').find('div.page-header'), 1200, {
                            offset: offsetheight
                        });
                        $(this).parents('section').next('section').find('div.page-header').focus();
                    }
                    else if ($(this).parents('form#mainform').next('section').length > 0) {
                        $.scrollTo($(this).parents('form#mainform').next('section').find('div.page-header'), 1200, {
                            offset: offsetheight
                        });
                        $(this).parents('form#mainform').next('section').find('div.page-header').focus();
                    }
                }
            }
            if (event.which === 13) {
                event.preventDefault();
            }
        }
    });

    // Keyboard navigation controls on section bars
    $('div.page-header', target).on("keydown", function(event) {

        var offsetheight = -($("div.subnav").height() + 42);

        if (event.ctrlKey && event.which === 40) {
            if ($(this).parents('section').next('section').length > 0) {
                $.scrollTo($(this).parents('section').next('section').find('div.page-header'), 1200, {
                    offset: offsetheight
                });
                $(this).parents('section').next('section').find('div.page-header').focus();
            }
            else if ($(this).parents('form#mainform').next('section').length > 0) {
                $.scrollTo($(this).parents('form#mainform').next('section').find('div.page-header'), 1200, {
                    offset: offsetheight
                });
                $(this).parents('form#mainform').next('section').find('div.page-header').focus();
            }
        }
        if (event.ctrlKey && event.which === 37) {
            if ($(this).siblings().is(':visible')) {
                $(this).siblings().css("display", "none");
                $(this).find('i.icon-chevron-right, i.icon-chevron-down').toggleClass("icon-chevron-right").toggleClass("icon-chevron-down");
                adjustscroll(target);
            }
        }
        if (event.ctrlKey && event.which === 39) {
            var that = $(this).siblings();
            if (that.is(':hidden')) {
                that.css("display", "inline-block");
                $(this).find('i.icon-chevron-right, i.icon-chevron-down').toggleClass("icon-chevron-right").toggleClass("icon-chevron-down");
                that.find(":input:visible:enabled:first").focus();
                adjustscroll(target);
            }
        }
        if (event.ctrlKey && event.which === 38) {
            if ($(this).parents('section').prev('section').length > 0) {
                $.scrollTo($(this).parents('section').prev('section').find('div.page-header'), 1200, {
                    offset: offsetheight
                });
                $(this).parents('section').prev('section').find('div.page-header').focus();
            }
            else if ($(this).parents('section').prev('form#mainform').length > 0) {
                $.scrollTo($(this).parents('section').prev('form#mainform').find('section').last().find('div.page-header'), 1200, {
                    offset: offsetheight
                });
                $(this).parents('section').prev('form#mainform').find('section').last().find('div.page-header').focus();
            }
        }
    });

    function hidethispage(target) {
        function getlocation(str) {
            if (str.slice(-1) === '/') {
                return str.slice(0, -1);
            }
            str = str.substr(str.lastIndexOf('/') + 1);
            return str;
        }
        var expander = $('.bigmenudropdown li.showalllinks', target);
        var location = getlocation(window.location.href);
        var links = $('.bigmenudropdown li.link', target).has("a[href]");
        var logout = $('.bigmenudropdown li.link', target).has("span.abbrev.logout");
        $(links).addClass('onlyfullview');
        for (var i = 0; i < links.length; i++) {
            if (getlocation($(links[i]).find('a').attr('href')) === location) {
                $(links[i]).remove();
                break;
            }
        }
        for(var j = 0; j < 9; j++){
            $(links[j]).removeClass('onlyfullview');
        }
        $(links[8]).after(logout);
        logout.removeClass('onlyfullview');
        $(logout).after(expander);
//        expander.remove();
    }

    function bigmenudropdownadjust(target) {
        if ($(window).width() > 979) {
            var viewportHeight = $(window).height();
            var navbarheight = $('div.navbar-fixed-top.navbar').height();
            var subnavheight = $('div#subnavscroll').height();
            var availableheight = viewportHeight - (navbarheight + subnavheight) - 10;
            if (availableheight < 424) {
                availableheight = 424;
            }
            $('.bigmenudropdown', target).css('max-height', '' + availableheight + 'px');
        }
        else {
            $('.bigmenudropdown', target).css('max-height', 'none');
        }
    }

    function abbreviatemenuoptions(target) {
        $('.bigmenudropdown .link', target).each(function() {
            var name = $(this).find('span.linktitle').text();
            var abbrev = name.substring(0, 2);
            $(this).find('span.imageplaceholder span.abbrev').text(abbrev);
        });
    }

    $('.bigmenudropdown', target).on('scroll', function() {
        if (!$(this).is('.showall')) {
            $('.bigmenudropdown', target).addClass('showall');
        }
    });
    $('.dropdown-menu.bigmenudropdown', target).on('click', function(e) {
        e.stopPropagation();
    });
    $('.dropdown-menu.bigmenudropdown .showalllinks', target).on('click', function() {
        $('.bigmenudropdown', target).toggleClass('showall');
        bigmenudropdownadjust(target);
    });
//    abbreviatemenuoptions(target);
    window.onresize = bigmenudropdownadjust;
    hidethispage(target);
}


/* $id$ */
/**
 * @class Old Tables
 */

/**
 * Used to initialise visual functionality to in row edit functionality for old inline edittable tables
 * @method oldInroweditTableInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @param {object} submitSetting Required global variale, set up in the screen specific config js. Paired to the row submits by use of data-submit settings attribute.
 * @version
 * @author Tom Yeldham
 * @return false On all buttons to prevent form submission by default
 * @deprecated
 */

// OLD INLINE EDIT TABLES

//No submit code at all, only visual currently

function oldInroweditTableInitialisation(target) {

    $('table tr button[data-submit-settings]', target).on('click', function () {
        if ($(this).hasClass('confirm-delete')) {
            if (confirm('Are you sure you want to delete this item?') === false)
                return false;
        }

        if ($(this).parents('tr').find(':input').valid()) {
            var submitsettings = $(this).data('submit-settings');
            var submitconfig = submitSetting[submitsettings];
            if (submitsettings === "" || submitconfig === undefined) {
                submitconfig = $(this).attributesAsArray("data-submit-");
            }
            submitconfig.form = $(this).parents('form');
            submitconfig.data = $(this).parents('tr').find(':input').serializeArray();
            $(this).trigger("rowsubmitinitialise", submitconfig);
            inroweditsubmit(submitconfig, $(this));
        }
        return false;
    });

    // Used for in row editting table row edit buttons. Shoes shadowbox, floats row about it, enables fields, focuses the first one and toggles the edittable buttons in
    $('.edittable', target).on("click", function () {
        edittable($(this), target);
        return false;
    });

    // Used for in row editting table row confirm and cancel buttons. Hides shadowbox, removes row's z-index, locks fields, removes all validation errors and toggles the edittable buttons out
    $('.locktable', target).on("click", function () {
        viewtable($(this), target);
        return false;
    });

    // Reveals new row in in row edittable tables. Does the same as edittable buttons
    $('.addnewrow', target).on("click", function () {
        var row = $(this).parents('table').find('tr:hidden:last');
        row.show();
        row.find(":input:visible:first").focus();
        $('button.edittable, .include, button.addbutton').attr('disabled', true);
        $('div.styled-select').not('table.table div.styled-select').addClass('disabled');
        row.find('div.styled-select').removeClass('disabled');
        $('.editmode, .viewmode').addClass('disabled');
        multiselectfix(target);
        sortoutaddons();
        adjustscroll(target);
        return false;
    });

    // Hides new row in in row edittable tables. Does the same as viewtable buttons
    $('.hidenewrow', target).on("click", function () {
        var row = $(this).parents('tr');
        row.find('.error').removeClass('error');
        row.find('div.help-block').remove();
        row.find(':input').val('');
        row.find('.con-multiselect').multiselect('uncheckAll');
        row.hide();
        $('button.edittable, .include, button.addbutton').attr('disabled', false);
        $('div.styled-select').not('table.table div.styled-select').removeClass('disabled');
        $('.editmode, .viewmode').removeClass('disabled');
        multiselectfix(target);
        sortoutaddons();
        adjustscroll(target);
        return false;
    });

}


/* $id$ */
/**
 * @class Page Init
 */

/**
 * Standard page element functionality initialisation
 * @method pageInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */

// PAGE INIT


function onetimeinit() {
    if (!window.init) {
        window.init = true;
        $(window).resize(function () {
            $('body').trigger('sortsubnavscroll');
            adjustscroll($('body'));
            headeralertfix();
            if ($('div.modal').is(':visible')) {
                $('div.modal:visible').find('.modal-body').trigger('adjustmodalbodyminheight');
            }
        });

        $('body').removeAttr('data-target').removeData('data-target').removeAttr('data-spy').removeData('data-spy');

// Initialise scrollspy
        if ($('#subnavscroll.subnav').length > 0) {
            $('body').scrollspy({
                target: '#subnavscroll',
                offset: $("div.subnav").height() + $('div.navbar:first div.navbar-inner').height()
            });
        }

// Makes the scrollspy tabs scroll the page to the correct location
        $('#subnavscroll li a').on('click', function (event) {
            adjustscroll($('body'));
            var height = $("div.subnav").height() + $('div.navbar:first div.navbar-inner').height();
            height = height - 2;
            var href = $(this).attr('href');
            if ($('div.subnav').css('position') === 'fixed') {
                event.preventDefault();
                window.location.hash = href;
                scrollBy(0, -height);
            } else {
                event.preventDefault();
                window.location.hash = href;
                scrollBy(0, -10);
            }
            if ($(href).find('.sectionbartoggle i').is('.icon-chevron-right')) {
                $(href).find('.sectionbartoggle').trigger('click');
            }
        });

        $('body').on('sortsubnavscroll', function () {
            // Uses the position attribute of the subnav scrollspy bar to determine if screen is being displayed on a tablet sized screen. If not then pad form so that the fixed headerbar doesnt over lap the contents
            var height = 0;
            var navbarheight = 0;
            if ($('div.subnav').length > 0) {
                if ($('div.subnav').css('position') === 'fixed') {
                    height = $("div.subnav").height();
                    navbarheight = $('.navbar.navbar-fixed-top').height();
                }
            } else {
                navbarheight = $('.navbar.navbar-fixed-top').height();
                navbarheight = navbarheight + 10;
            }
            height = height + navbarheight;
            //if($('div.subnav').parent().next().is(".alert")) {
            //    height = height * 2;
            //}
            $("div.subnav").css('top', navbarheight + "px");
            $('body').css('padding-top', height + "px");
            adjustscroll($('body'));
        });
        if (get_browser() === "Firefox" && get_browser_version() === "14") {
            $('li.bigmenu.dropdown').remove();
        }
    }
}


$(document).ready(function () {
    onetimeinit();
    $('body').trigger('sortsubnavscroll');
});

var chrome = (/chrom(e|ium)/.test(navigator.userAgent.toLowerCase()));

var tour;

function pageInitialisation(target) {

    // Bind tutorial on click. Make sure the data-settings attribute exits so we can load the tutorial settings via ajax
    $(".ui-tutorial[data-settings]", target).on('click', function (e) {
        // This should include the protocal, port, domain and path.
        var settingsURL = $(this).data('settings');

        //E.g: https://cdn.metafour.com/tuts/devel/netcourier-online/booking/full-booking-screen-en.json
        $.getJSON(settingsURL + "?callback=?", function (data) {

            //Specify global Tour settings.
            data.storage = false;
            var currentlyat = 0;
            data.onShown = function (tour) {
                if ($(this.element).data().hasOwnProperty("tooltip")) {
                    if (currentlyat !== this.next - 1) {
                        currentlyat = this.next - 1;
                        tour.goTo(this.next - 1);
                    } else {
                        $(this.element).tooltip('show');
                    }
                }
            };
            data.onHidden = function (tour) {
                if ($(this.element).data().hasOwnProperty("tooltip")) {
                    $(this.element).tooltip('hide');
                }
            };

            // Load the settings into the tour.
            tour = new Tour(data);

            // Initialise the tour
            tour.init();

            // Start the tour
            tour.start(true);

        }).fail(function () {
            alert('Unable to fetch tutorial information.');
        }); //End of getJSON

        // Close the menu dropdown automatically (it stays open otherwise)
        $('#menudropdown .dropdown-toggle').dropdown('toggle');

        // Prevent default click behaviour.
        e.preventDefault();
        return false;
    });

    $('input[type="button"], button', target).on("keypress", function (event) {
        if (event.keyCode === 13) {
            $(this).trigger('click');
            return false;
        }
    });

    $('div.modal', target).on('loaded', function () {
        setvalidator();
//        $(this).find('.modal-body,.modal-footer').off();
        metaboot($(this));
        editmode($(this));
        addvalidation($(this));
    });

    $('select option:selected[data-status="D"]', target).each(function () {
        $(this).parents('div.controls').addClass('archiveError');
    });

    // Sets arrow on section bars correctly depending on if they are hidden or not on load
    $('div.page-header', target).each(function () {
        if ($(this).siblings('div.row:visible').length < 1) {
            $(this).find('i.icon-chevron-down').toggleClass("icon-chevron-right").toggleClass("icon-chevron-down");
        }
    });
    $('body').trigger('sortsubnavscroll');
    $('#optionstabs').tab();

    var focuselements = $('.focusonload');
    focuselements.first().focus();
    focuselements.removeClass('focusonload');
}



/* $id$ */
/**
 * @class Rich Text Editors
 */

/**
 * Initialises all rte textareas on the page
 * @method rteInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */
function rteInitialisation(target) {

    $('textarea.rte', target).each(function() {
        var parentelement = $(this).parent();
        $(this).wysihtml5({
            "image": false,
            "stylesheets": false,
            "events": {
                "load": function() {
                    if ($('.navbar-fixed-top li a.viewmode:visible').length > 1) {
                        enablerte(parentelement);
                    }
                    else {
                        disablerte(parentelement);
                    }
                }
            }
        });
    });

    $('.bootstrap-wysihtml5-insert-link-modal', target).on('shown', function(e) {
        e.stopPropagation();
    }).on('hidden', function(e) {
        e.stopPropagation();
    });

    $('.disablerte', target).on('click', function() {
        var targettexteditorcontainer = $(this).parents('div.modal');
        disablerte(targettexteditorcontainer);
    });

    $('.enablerte', target).on('click', function() {
        var targettexteditorcontainer = $(this).parents('div.modal');
        enablerte(targettexteditorcontainer);
    });

}

function loadintorte(targetrte, datatoload) {
    targetrte.data('wysihtml5').editor.setValue(datatoload);
}



/* $id$ */
/**
 * @class Search Suggests
 */
/**
 * Used to initialise searchsuggest fields
 * @method searchSuggestInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @param {object} searchSuggestSetting Required global variable from screen specific searchconfig.js. Contains a series of sets of parameters, each with a unique key. Each input is paired to the desired set of parameters by attaching a search-settings data attribute to it containing the key of the set that is required. See examples below.
 * @version
 * @author Tom Yeldham
 * @return false For multi search suggests
 *
 * @example <input type="text" autocomplete="off" placeholder="Search" class="typeahead search-query input-large" data-search-settings="navbarsearch">
 *
 * The header navbar has the above mark up
 *
 * The data-search-settings attribute value is what is used to pair this particular field to the desired set of parameters in searchconfig.js. We can see it has a value of navbarsearch, which corresponds to
 *
 * @example
 * "navbarsearch": {
 * // Set the url to your service and the searchid to that of the type service you require from that service
 * url: "http://maf.dev.m4.net/searchSuggest-1.0/newSearchSuggestServlet",
 * dataType: "jsonp",
 * dataparams: [{
 * key:"id", value:"PROPERTY1"
 * }],
 * success: function( responseData, resultsList) {
 * for(i=0;i<responseData.length;i++){
 * record = {};
 * record["key"] = responseData[i].key;
 * if($.isArray(responseData[i].value)){
 * record["value"] = [];
 * for(j=0;
 * j<responseData[i].value.length;
 * j++){
 * record["value"][j] = responseData[i].value[j];
 * }
 * }
 * resultsList.push(record)
 * }
 * },
 renderItem: function (ul, data) {
 * return $("<li class='columnautocomplete'></li>")
 * .data("item.autocomplete", data)
 * //Format the below line to contain the desired amount of columns and ensure that you are selecting the desired array value for each column
 * .append("<a><div class='col'>" + data.value[0] + "</div><div class='col'>" + data.value[1] + "</div></a>")
 * .appendTo(ul);
 * }
 * },
 *
 * url, dataparans and dataType are all compulsary. dataparams must be an array of key value pairs formatted as in the example. beforesend, complete, success, error and select are all optional if you wish to alter the default behaviour of the plugin which is configured to work with the service I have been testing against, by using a different function.
 * If the searchsuggest is required to display tabular data, then you must add a renderItem parameter in your config for that key. It obviously has to fit with the format of resultsList array that you are providing to the response callback.
 * They should be configured to conform as the jQuery ajax() settings of the same name. See the following link for full documentation http://api.jquery.com/jQuery.ajax/.
 *
 * If additional parameters are required it is possible to completely override the template and provide a unique complete autocomplete initilaise for that searchsuggest by giving a search config key a parameter of uniqueinitialise. This should be a full initialise function and obviously shouldnt include any other parameters.
 *
 * An optional 'heading' object can be sent in the JSON. This can be picked up in the success function and formatted in the renderItem function. See above.
 *
 * Example JSON:
 * <code> { headings: ['Place','City'], values: [{"value":["Africa EMB","Cape Town"],"key":"AFRICAEMB"},{"value":["Arthurs Seat","Cape Town"],"key":"ARS"}] }; </code>
 */
//SEARCHES

var xhr = null;

function searchSuggestInitialisation(target) {
    $(".chargepergclicksearch", target).each(function () {
        var searchfield = $(this);
        // Load values form template target data search attribute
        var searchSuggestSettingsSource = $(this).attributesAsArray("data-search-");
        var settingid = searchSuggestSettingsSource.settings;
        // set type ahread desc
        searchfield.siblings(".typeahead-desc").val(searchfield.val());
        // Load from pre-defined configuration
        var preDefinedSettingsSource = searchSuggestSetting[settingid];
        if (preDefinedSettingsSource !== undefined) {
            for (var key in preDefinedSettingsSource) {
                if (!searchSuggestSettingsSource[key]) {
                    searchSuggestSettingsSource[key] = preDefinedSettingsSource[key];
                }
            }
        }
        // Load values from searchSuggestSetting from defaults in case not already there
        preDefinedSettingsSource = searchSuggestSetting['default'];
        for (var key2 in preDefinedSettingsSource) {
            if (!searchSuggestSettingsSource[key2]) {
                searchSuggestSettingsSource[key2] = preDefinedSettingsSource[key2];
            }
        }
        searchfield.trigger("searchsuggestinitialise", searchSuggestSettingsSource);
        var uniqueinitialise = searchSuggestSettingsSource.uniqueinitialise;
        var searchurl = searchSuggestSettingsSource.url;
        var searchstaticurl = searchSuggestSettingsSource.staticurl;
        var searchdataparams = searchSuggestSettingsSource.dataparams;
        var searchdependentfields = searchSuggestSettingsSource.dependentfields;
        if (searchdependentfields !== undefined) {
            searchdependentfields = searchdependentfields.split(";");
        }
        var searchdatatype = searchSuggestSettingsSource.dataType;
        var searchsuccess = searchSuggestSettingsSource.success;
        var searcherror = searchSuggestSettingsSource.error;
        var searchselect = searchSuggestSettingsSource.select;
        var searchselectiondone = searchSuggestSettingsSource.selectiondone;
        var renderItem = searchSuggestSettingsSource.renderItem;
        var beforesend = searchSuggestSettingsSource.beforesend;
        var complete = searchSuggestSettingsSource.complete;
        if (uniqueinitialise !== undefined) {
            searchfield.uniqueinitialise();
        } else {
            beforesend = getExecutableFunction(beforesend);
            complete = getExecutableFunction(complete);
            searchsuccess = getExecutableFunction(searchsuccess, function (responseData, response) {
                var resultsList = [];
                var headerrecord = {};
                if (responseData.headings) {
                    headerrecord.headings = responseData.headings;
                    resultsList.push(headerrecord);
                }
                //Push the main values object to the root of respinseData. This will blow away the headings
                //but that is OK.
                if ($.isArray(responseData.values)) {
                    responseData = responseData.values;
                }
                for (var i = 0; i < responseData.length; i++) {
                    record = {};
                    record.key = responseData[i].key;
                    record.multiColumn = false;
                    if ($.isArray(responseData[i].value)) {
                        record.multiColumn = true;
                        record.value = [];
                        for (var j = 0; j < responseData[i].value.length; j++) {
                            record.value[j] = escapeHtml(responseData[i].value[j]);
                        }
                    } else {
                        record.value = escapeHtml(responseData[i].value);
                    }
                    record.data = responseData[i].data;
                    resultsList.push(record);
                }
                response(resultsList);
            });
            searcherror = getExecutableFunction(searcherror, function () {
                alert("We're sorry, something went wrong.");
            });
            searchselectiondone = getExecutableFunction(searchselectiondone);
            searchselect = getExecutableFunction(searchselect, function (event, ui) {
                if ($(event.currentTarget).find('li:has("a.ui-state-focus")').hasClass('addnew')) {
                    var targetmodal = $(event.currentTarget).find('li.addnew div').attr('data-target');
                    $(targetmodal).modal('show');
                } else {
                    searchfield.siblings(".typeahead-val").val(ui.item.key);
                    searchfield.siblings(".typeahead-desc").val(ui.item.value);
                    searchfield.parent().siblings(".typeahead-val").val(ui.item.key);
                    searchfield.parent().siblings(".typeahead-desc").val(ui.item.value);
                    if (searchfield.parent().is("form")) {
                        searchfield.parents("form").attr("action", searchfield.parents("form").attr("action") + ui.item.key);
                        searchfield.parents("form").submit();
                    }
                }
                searchselectiondone(event, ui);
                searchfield.trigger("searchsuggestselectiondone", ui);
            });
            if (searchdatatype === undefined) {
                searchdatatype = "json";
            }
            searchfield.autocomplete({
                minLength: 1,
                delay: 500,
                messages: {
                    noResults: '',
                    results: function () {
                    }
                },
                position: {
                    my: "left top",
                    at: "left bottom",
                    collision: "flip"},
                search: function (event, ui) {
                    searchfield.parents('.controls').find('i.icon-search:first').addClass('autocompletesearching');
                },
                response: function (event, ui) {
                    searchfield.parents('.controls').find('i.icon-search.autocompletesearching').removeClass('autocompletesearching');
                },
                source: function (request, response) {
                    request.term = request.term === "?" ? encodeURIComponent(request.term) : request.term;
                    var data = {
                        search: request.term
                    };
                    // Make global var parameter an array of key value pairs [{a, 1},{b. 2}]
                    if (searchdataparams !== undefined) {
                        for (var searchparamcounter = 0; searchparamcounter < searchdataparams.length; searchparamcounter++) {
                            var param = searchdataparams[searchparamcounter];
                            var key = param.key;
                            var val = param.value;
                            data[key] = val;
                        }
                    }
                    // Read param and value source dependent field id pairs like destination,availdestination;resort,availresort
                    if (searchdependentfields !== undefined) {
                        for (var searchdependentfieldscounter = 0; searchdependentfieldscounter < searchdependentfields.length; searchdependentfieldscounter++) {
                            var fieldparam = searchdependentfields[searchdependentfieldscounter].split(",");
                            var fieldkey = fieldparam[0];
                            var fieldval = $("#" + fieldparam[1]).val();
                            data[fieldkey] = fieldval;
                        }
                    }
                    var config = {
                        url: searchurl,
                        data: data
                    };
                    var builturl;
                    searchfield.trigger("searchsuggestpresearch", config);
                    if (!searchstaticurl) {
                        builturl = config.url + replace_slash(config.data.search);
                    } else {
                        builturl = searchstaticurl;
                    }
                    if (xhr)
                        xhr.abort();
                    xhr = $.ajax({
                        complete: complete,
                        beforeSend: beforesend,
                        async: true,
                        url: builturl,
                        dataType: searchdatatype,
                        data: config.data,
                        success: function (responseData) {
                            if (responseData.values.length > 13) {

                            }
                            searchsuccess(responseData, response);
                            //searchfield.autocomplete("disable");
                        },
                        error: function (data, textStatus, errorThrown) {
                            searchfield.parents('.controls').find('i.icon-search.autocompletesearching').removeClass('autocompletesearching');
                            var html = $.parseHTML(data.responseText);
                            if (html) {
                                $.each(html, function (i, el) {
                                    if (el.nodeName.toLowerCase() === "meta") {
                                        if (el.name && el.name.toLowerCase() === "login") { // looks like login required
                                            var loginRequestText = el.getAttribute('content');
                                            var redirectURL = el.getAttribute('data-href');
                                            if (loginRequestText)
                                                alert(loginRequestText);
                                            if (redirectURL) {
                                                window.location.href = redirectURL;
                                                return false;
                                            }
                                        }
                                    }
                                });
                                searcherror(data);
                            }
                        }
                    });
                },
                select: function (event, ui) {
                    searchselect(event, ui);
                    $(this).autocomplete("disable");

                    $('ul.ui-autocomplete').attr("donot-allow-for-a-while", "Y");	// do not allow search                    
                    setTimeout(function () {
                        $('ul.ui-autocomplete').attr("donot-allow-for-a-while", "N");	// clear it after 100 mili-seconds
                    }, 100);

                    console.log("On select:" + $(this).autocomplete("option", "disabled"));

                    return false;
                },
                close: function (event, ui) {
                    $(this).autocomplete("disable");
                }
            });
            if (renderItem !== undefined) {
                searchfield.data("ui-autocomplete")._renderItem = renderItem;
            }
            searchfield.data("ui-autocomplete")._renderMenu = function (ul, items) {
                var that = this;
                $.each(items, function (index, item) {
                    that._renderItemData(ul, item);
                });
                $(ul).find("li:odd").addClass("odd");
                if (items.length > 13) {
                    $(ul).addClass('extend');
                } else {
                    $(ul).removeClass('extend');
                }
            };
        }
    });
    $(".chargepergclicksearch").on("keydown", function (event) {

        if ($('ul.ui-autocomplete').attr("donot-allow-for-a-while") !== 'Y') {
            $('ul.ui-autocomplete').attr("donot-allow-for-a-while", "N");
        }
        if (event.which === 13) {
            if ($('ul.ui-autocomplete').attr("donot-allow-for-a-while") !== 'Y') {
                $(this).autocomplete("enable");
                $(this).autocomplete("search");
            }

        } else {
            if (event.which !== 38 && event.which !== 40) {
                $(this).autocomplete("disable");
                $('ul.ui-autocomplete').hide();
            }
        }

    }).on("focus", function () {
        $(this).autocomplete("disable");
        $('ul.ui-autocomplete').hide();
    });
    $(".chargepergclicksearchtrigger").on('click', function () {
        $(this).parents('.input-append').find('.chargepergclicksearch').autocomplete("enable");
        $(this).parents('.input-append').find('.chargepergclicksearch').autocomplete("search");
    });

    $(".typeahead", target).each(function () {
        var searchfield = $(this);
        // Load values form template target data search attribute
        var searchSuggestSettingsSource = $(this).attributesAsArray("data-search-");
        var settingid = searchSuggestSettingsSource.settings;
        // set type ahread desc
        searchfield.siblings(".typeahead-desc").val(searchfield.val());
        // Load from pre-defined configuration
        var preDefinedSettingsSource = searchSuggestSetting[settingid];
        if (preDefinedSettingsSource !== undefined) {
            for (var key in preDefinedSettingsSource) {
                if (!searchSuggestSettingsSource[key]) {
                    searchSuggestSettingsSource[key] = preDefinedSettingsSource[key];
                }
            }
        }
        // Load values from searchSuggestSetting from defaults in case not already there
        preDefinedSettingsSource = searchSuggestSetting['default'];
        for (var key2 in preDefinedSettingsSource) {
            if (!searchSuggestSettingsSource[key2]) {
                searchSuggestSettingsSource[key2] = preDefinedSettingsSource[key2];
            }
        }
        searchfield.trigger("searchsuggestinitialise", searchSuggestSettingsSource);
        var uniqueinitialise = searchSuggestSettingsSource.uniqueinitialise;
        var searchurl = searchSuggestSettingsSource.url;
        var searchstaticurl = searchSuggestSettingsSource.staticurl;
        var searchdataparams = searchSuggestSettingsSource.dataparams;
        var searchdependentfields = searchSuggestSettingsSource.dependentfields;
        if (searchdependentfields !== undefined) {
            searchdependentfields = searchdependentfields.split(";");
        }
        var searchdatatype = searchSuggestSettingsSource.dataType;
        var searchsuccess = searchSuggestSettingsSource.success;
        var searcherror = searchSuggestSettingsSource.error;
        var searchselect = searchSuggestSettingsSource.select;
        var searchselectiondone = searchSuggestSettingsSource.selectiondone;
        var renderItem = searchSuggestSettingsSource.renderItem;
        var beforesend = searchSuggestSettingsSource.beforesend;
        var complete = searchSuggestSettingsSource.complete;
        var searchminlength = searchSuggestSettingsSource.minLength;
        var searchdelay = searchSuggestSettingsSource.delay;
        if (uniqueinitialise !== undefined) {
            searchfield.uniqueinitialise();
        } else {
            beforesend = getExecutableFunction(beforesend);
            complete = getExecutableFunction(complete);
            searchsuccess = getExecutableFunction(searchsuccess, function (responseData, response) {
                var resultsList = [];
                var headerrecord = {};
                if (responseData.headings) {
                    headerrecord.headings = responseData.headings;
                    resultsList.push(headerrecord);
                }
				//Push the main values object to the root of respinseData. This will blow away the headings
				//but that is OK.
                if ($.isArray(responseData.values)) {
                    responseData = responseData.values;
                }
                for (var i = 0; i < responseData.length; i++) {
                    record = {};
                    record.key = responseData[i].key;
                    record.multiColumn = false;
                    if ($.isArray(responseData[i].value)) {
                        record.multiColumn = true;
                        record.value = [];
                        for (var j = 0; j < responseData[i].value.length; j++) {
                            record.value[j] = escapeHtml(responseData[i].value[j]);
                        }
                    } else {
                        record.value = escapeHtml(responseData[i].value);
                    }
                    record.data = responseData[i].data;
                    resultsList.push(record);
                }
                response(resultsList);
            });
            searcherror = getExecutableFunction(searcherror, function () {
                alert("We're sorry, something went wrong.");
            });
            searchselectiondone = getExecutableFunction(searchselectiondone);
            searchselect = getExecutableFunction(searchselect, function (event, ui) {
                if ($(event.currentTarget).find('li:has("a.ui-state-focus")').hasClass('addnew')) {
                    var targetmodal = $(event.currentTarget).find('li.addnew div').attr('data-target');
                    $(targetmodal).modal('show');
                } else {
                    searchfield.siblings(".typeahead-val").val(ui.item.key);
                    searchfield.siblings(".typeahead-desc").val(ui.item.value);
                    searchfield.parent().siblings(".typeahead-val").val(ui.item.key);
                    searchfield.parent().siblings(".typeahead-desc").val(ui.item.value);
                    if (searchfield.parent().is("form")) {
                        if (searchfield.parent().is('form.navbar-search') && "data" in ui.item) {
                            searchfield.parents("form").attr("action", ui.item.data + ui.item.key);
                        } else {
                            searchfield.parents("form").attr("action", searchfield.parents("form").attr("action") + ui.item.key);
                        }
                        searchfield.parents("form").submit();
                    }
                }
                searchselectiondone(event, ui);
                searchfield.trigger("searchsuggestselectiondone", ui);
            });
            if (searchdatatype === undefined) {
                searchdatatype = "json";
            }
            if (searchminlength === undefined) {
                searchminlength = 1;
            }
            if (searchdelay === undefined) {
                searchdelay = 500;
            }
            searchfield.autocomplete({
                minLength: searchminlength,
                delay: searchdelay,
                autoFocus: true,
                messages: {
                    noResults: '',
                    results: function () {
                    }
                },
                position: {
                    my: "left top",
                    at: "left bottom",
                    collision: "flip"},
                search: function (event, ui) {
                    searchfield.parents('.controls').find('i.icon-search:first').addClass('autocompletesearching');
                },
                response: function (event, ui) {
                    searchfield.parents('.controls').find('i.icon-search.autocompletesearching').removeClass('autocompletesearching');
                },
                source:
                        function (request, response) {
                            request.term = request.term === "?" ? encodeURIComponent(request.term) : request.term;
                            var data = {
                                search: request.term
                            };
                            // Make global var parameter an array of key value pairs [{a, 1},{b. 2}]
                            if (searchdataparams !== undefined) {
                                for (var searchparamcounter = 0; searchparamcounter < searchdataparams.length; searchparamcounter++) {
                                    var param = searchdataparams[searchparamcounter];
                                    var key = param.key;
                                    var val = param.value;
                                    data[key] = val;
                                }
                            }
                            // Read param and value source dependent field id pairs like destination,availdestination;resort,availresort
                            if (searchdependentfields !== undefined) {
                                for (var searchdependentfieldscounter = 0; searchdependentfieldscounter < searchdependentfields.length; searchdependentfieldscounter++) {
                                    var fieldparam = searchdependentfields[searchdependentfieldscounter].split(",");
                                    var fieldkey = fieldparam[0];
                                    var fieldval = $("#" + fieldparam[1]).val();
                                    data[fieldkey] = fieldval;
                                }
                            }
                            var config = {
                                url: searchurl,
                                data: data
                            };
                            var builturl;
                            searchfield.trigger("searchsuggestpresearch", config);
                            if (!searchstaticurl) {
                                builturl = config.url + replace_slash(config.data.search);
                            } else {
                                builturl = searchstaticurl;
                            }
                            if (xhr)
                                xhr.abort();
                            xhr = $.ajax({
                                complete: complete,
                                beforeSend: beforesend,
                                async: true,
                                url: builturl,
                                dataType: searchdatatype,
                                data: config.data,
                                success: function (responseData) {
                                    searchsuccess(responseData, response);
                                },
                                error: function (data, textStatus, errorThrown) {
                                    searchfield.parents('.controls').find('i.icon-search.autocompletesearching').removeClass('autocompletesearching');
                                    var html = $.parseHTML(data.responseText);
                                    if (html) {
                                        $.each(html, function (i, el) {
                                            if (el.nodeName.toLowerCase() === "meta") {
                                                if (el.name && el.name.toLowerCase() === "login") { // looks like login required
                                                    var loginRequestText = el.getAttribute('content');
                                                    var redirectURL = el.getAttribute('data-href');
                                                    if (loginRequestText)
                                                        alert(loginRequestText);
                                                    if (redirectURL) {
                                                        window.location.href = redirectURL;
                                                        return false;
                                                    }
                                                }
                                            }
                                        });
                                        searcherror(data);
                                    }
                                }
                            });
                        },
                select: function (event, ui) {
                    searchselect(event, ui);
                }
            });
            if (renderItem !== undefined) {
                searchfield.data("ui-autocomplete")._renderItem = renderItem;
            }
            searchfield.data("ui-autocomplete")._renderMenu = function (ul, items) {
                var that = this;
                $.each(items, function (index, item) {
                    that._renderItemData(ul, item);
                });
                $(ul).find("li:odd").addClass("odd");
                if (items.length > 13) {
                    $(ul).addClass('extend');
                } else {
                    $(ul).removeClass('extend');
                }
            };
        }
    });
    $(".typeahead", target).on("blur", function () {
        var item = $(this);
        if (item.val().trim() === "" || item.siblings(".typeahead-val").val().trim() === "" ||
                item.siblings(".typeahead-val").val().trim() === "" || item.val().trim() !== item.siblings(".typeahead-desc").val().trim()) {
            item.val("");
            item.siblings(".typeahead-val").val("");
            item.siblings(".typeahead-desc").val("");
        }
    }).on('keypress', function (e) {
        if (e.which === 13) {
            $(this).autocomplete("search");
        }
    });
    $(".chargepergclicksearch").on("blur", function () {
        var item = $(this);
        setTimeout(function () {
            if ($(':focus').hasClass("chargepergclicksearchtrigger"))
                return;
            if (item.val().trim() === "" || item.siblings(".typeahead-val").val().trim() === "" ||
                    item.siblings(".typeahead-val").val().trim() === "" || item.val().trim() !== item.siblings(".typeahead-desc").val().trim()) {
                //    item.val("");
                //    item.siblings(".typeahead-val").val("");
                //    item.siblings(".typeahead-desc").val("");
            }
        }, 200);
    });
    $(".chargepergclicksearchtrigger").on("blur", function () {
        $('ul.ui-autocomplete').hide();
    });
    //TODO: need to add in metafour-ui
    $('[data-target="#addressmodal"]').each(function () {
        $(this).off("click");
        $(this).on("click", function (event) {
            if ($('[data-target="#addressmodal"].modal-opened').html()) {
                $('[data-target="#addressmodal"].modal-opened').removeClass('modal-opened');
            }
            var originalfield = event.target;
            var selectedradiolabel = $(originalfield).parents('div.controls').find('label:first-child');
            var selectedradio = selectedradiolabel.find('input[type="radio"]');
            $('#targetdiv').val(event.target.id);
            // TODO Integrate
            var addressId = selectedradio.length > 0 ? selectedradio.val() : '';
            var url = $(this).attr('data-remote') + addressId;
            $("#addressmodal").load(url);
            $("#addressmodal").modal('show');
            $(this).addClass('modal-opened');
            return false;
        });
    });
}

function replace_slash(str) {
    return String(str).replace(new RegExp('/', 'g'), '@@ESCAPE@@');
}


/* $id$ */
/**
 * @class Tables
 */

/*jshint multistr: true */

/**
 * Generic table initialisation set up
 * @method tableInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */
function tableInitialisation(target) {

    $('.ajax-table', target).each(function () {
        bangingtables($(this));
    });

    /* API method to get paging information */
    $.fn.dataTableExt.oApi.fnPagingInfo = function (oSettings)
    {
        return {
            "iStart": oSettings._iDisplayStart,
            "iEnd": oSettings.fnDisplayEnd(),
            "iLength": oSettings._iDisplayLength,
            "iTotal": oSettings.fnRecordsTotal(),
            "iFilteredTotal": oSettings.fnRecordsDisplay(),
            "iPage": oSettings._iDisplayLength === -1 ?
                    0 : Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
            "iTotalPages": oSettings._iDisplayLength === -1 ?
                    0 : Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength)
        };
    };


    $.extend($.fn.dataTableExt.oStdClasses, {
        'sPageEllipsis': 'paginate_ellipsis',
        'sPageNumber': 'paginate_number',
        'sPageNumbers': 'paginate_numbers'
    });

    $.fn.dataTableExt.oPagination.ellipses = {
        'oDefaults': {
            'iShowPages': 5
        },
        'fnClickHandler': function (e) {
            var fnCallbackDraw = e.data.fnCallbackDraw,
                    oSettings = e.data.oSettings,
                    sPage = e.data.sPage;

            if ($(this).is('[disabled]')) {
                return false;
            }

            oSettings.oApi._fnPageChange(oSettings, sPage);
            fnCallbackDraw(oSettings);

            return true;
        },
        // fnInit is called once for each instance of pager
        'fnInit': function (oSettings, nPager, fnCallbackDraw) {
            var oPaging = oSettings.oInstance.fnPagingInfo();
            var oClasses = oSettings.oClasses,
                    oLang = oSettings.oLanguage.oPaginate,
                    that = this;
            oClasses.sPageFirst = "firstpage";
            oClasses.sPagePrevious = "prev";
            oClasses.sPageNext = "next";
            oClasses.sPageLast = "lastpage";
            var iShowPages = oSettings.oInit.iShowPages || this.oDefaults.iShowPages,
                    iShowPagesHalf = Math.floor(iShowPages / 2);

            $.extend(oSettings, {
                _iShowPages: iShowPages,
                _iShowPagesHalf: iShowPagesHalf
            });

            var oFirst = $('<a class="' + oClasses.sPageButton + ' ' + oClasses.sPageFirst + '">1</a>'),
                    oPrevious = $('<a class="' + oClasses.sPageButton + ' ' + oClasses.sPagePrevious + '">' + oLang.sPrevious + '</a>'),
                    oNumbers = $('<span class="' + oClasses.sPageNumbers + '"></span>'),
                    oNext = $('<a class="' + oClasses.sPageButton + ' ' + oClasses.sPageNext + '">' + oLang.sNext + '</a>'),
                    oLast = $('<a class="' + oClasses.sPageButton + ' ' + oClasses.sPageLast + '">' + oPaging.iTotalPages + '</a>');

            oFirst.click({'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': 'first'}, that.fnClickHandler);
            oPrevious.click({'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': 'previous'}, that.fnClickHandler);
            oNext.click({'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': 'next'}, that.fnClickHandler);
            oLast.click({'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': 'last'}, that.fnClickHandler);

            // Draw
            $(nPager).append(oPrevious, oFirst, oNumbers, oLast, oNext);
        },
        // fnUpdate is only called once while table is rendered
        'fnUpdate': function (oSettings, fnCallbackDraw) {
            var oClasses = oSettings.oClasses,
                    that = this;

            var tableWrapper = oSettings.nTableWrapper;

            // Update stateful properties
            this.fnUpdateState(oSettings);

            if (oSettings._iCurrentPage === 1) {
                $('.' + oClasses.sPageFirst, tableWrapper).attr('disabled', true);
                $('.' + oClasses.sPagePrevious, tableWrapper).attr('disabled', true);
            } else {
                $('.' + oClasses.sPageFirst, tableWrapper).removeAttr('disabled');
                $('.' + oClasses.sPagePrevious, tableWrapper).removeAttr('disabled');
            }

            if (oSettings._iTotalPages === 0 || oSettings._iCurrentPage === oSettings._iTotalPages) {
                $('.' + oClasses.sPageNext, tableWrapper).attr('disabled', true);
                $('.' + oClasses.sPageLast, tableWrapper).attr('disabled', true);
            } else {
                $('.' + oClasses.sPageNext, tableWrapper).removeAttr('disabled');
                $('.' + oClasses.sPageLast, tableWrapper).removeAttr('disabled');
            }

            var i, oNumber, oNumbers = $('.' + oClasses.sPageNumbers, tableWrapper);

            $(oNumbers).parents('.dataTables_paginate').show();

            // Erase
            oNumbers.html('');

            for (i = oSettings._iFirstPage + 1; i <= oSettings._iLastPage - 1; i++) {
                oNumber = $('<a class="' + oClasses.sPageButton + ' ' + oClasses.sPageNumber + '">' + oSettings.fnFormatNumber(i) + '</a>');

                if (oSettings._iCurrentPage === i) {
                    oNumber.attr('active', true).attr('disabled', true);
                } else {
                    oNumber.click({'fnCallbackDraw': fnCallbackDraw, 'oSettings': oSettings, 'sPage': i - 1}, that.fnClickHandler);
                }

                // Draw
                oNumbers.append(oNumber);
            }

            // Add ellipses
            if (1 < oSettings._iFirstPage) {
                oNumbers.prepend('<span class="' + oClasses.sPageEllipsis + '">...</span>');
            }

            if (oSettings._iLastPage < oSettings._iTotalPages) {
                oNumbers.append('<span class="' + oClasses.sPageEllipsis + '">...</span>');
            }

            if (oSettings._iLastPage === 1 || oSettings._iTotalPages === 0) {
                $(oNumbers).parents('.dataTables_paginate').hide();
            }
            $(oNumbers).parents('.dataTables_paginate').find('a.lastpage').text(oSettings._iTotalPages);
        },
        // fnUpdateState used to be part of fnUpdate
        // The reason for moving is so we can access current state info before fnUpdate is called
        'fnUpdateState': function (oSettings) {
            var iCurrentPage = Math.ceil((oSettings._iDisplayStart + 1) / oSettings._iDisplayLength),
                    iTotalPages = Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength),
                    iFirstPage = iCurrentPage - oSettings._iShowPagesHalf,
                    iLastPage = iCurrentPage + oSettings._iShowPagesHalf;

            if (iTotalPages < oSettings._iShowPages) {
                iFirstPage = 1;
                iLastPage = iTotalPages;
            } else if (iFirstPage < 1) {
                iFirstPage = 1;
                iLastPage = oSettings._iShowPages;
            } else if (iLastPage > iTotalPages) {
                iFirstPage = (iTotalPages - oSettings._iShowPages) + 1;
                iLastPage = iTotalPages;
            }

            $.extend(oSettings, {
                _iCurrentPage: iCurrentPage,
                _iTotalPages: iTotalPages,
                _iFirstPage: iFirstPage,
                _iLastPage: iLastPage
            });
        }
    };




//    $('table:not(".datatabled")', target).each(function(){
//        if ($(this).parent(':not("div.tablewrapper")')){
//            $(this).wrap('<div class="tablewrapper"></div>');
//        }
//    });

    /* Set the defaults for DataTables initialisation */
    $.extend(true, $.fn.dataTable.defaults, {
//        "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
        "sDom": "<'row-fluid'<'pull-left'l><'pull-right'f>r><'tablewrapper't><'row-fluid toptweak'<'pull-left'i><'pull-right'p>>",
        "sPaginationType": "ellipses"
    });

    /* Create an array with the values of all the input boxes in a column */
    $.fn.dataTable.ext.order['dom-textarea'] = function (settings, col)
    {
        return this.api().column(col, {order: 'index'}).nodes().map(function (td, i) {
            return $('textarea', td).val();
        });
    };

    /* Create an array with the values of all the input boxes in a column */
    $.fn.dataTable.ext.order['dom-text'] = function (settings, col)
    {
        return this.api().column(col, {order: 'index'}).nodes().map(function (td, i) {
            return $('input', td).val();
        });
    };

    /* Create an array with the values of all the input boxes in a column, parsed as numbers */
    $.fn.dataTable.ext.order['dom-text-numeric'] = function (settings, col)
    {
        return this.api().column(col, {order: 'index'}).nodes().map(function (td, i) {
            return $('input', td).val() * 1;
        });
    };

    /* Create an array with the values of all the select options in a column */
    $.fn.dataTable.ext.order['dom-select'] = function (settings, col)
    {
        return this.api().column(col, {order: 'index'}).nodes().map(function (td, i) {
            return $('select option:selected', td).text();
        });
    };

    /* Create an array with the values of all the checkboxes in a column */
    $.fn.dataTable.ext.order['dom-checkbox'] = function (settings, col)
    {
        return this.api().column(col, {order: 'index'}).nodes().map(function (td, i) {
            return $('input', td).prop('checked') ? '1' : '0';
        });
    };

    /* Create an array with the values of all the datepickers in a column */
    $.fn.dataTable.ext.order['dom-datesedittable'] = function (settings, col)
    {
        return this.api().column(col, {order: 'index'}).nodes().map(function (td, i) {
            return  moment.utc($('input.con-datepicker, input.con-startdate, input.con-enddate', td).val(), m4dateformat.moment);
        });
    };
    /* Create an array with the values of all the checkboxes in a column */
    $.fn.dataTable.ext.order['dom-datestatic'] = function (settings, col)
    {
        return this.api().column(col, {order: 'index'}).nodes().map(function (td, i) {
            return moment($('span[class*="date"], span[name*="date"]', td).text(), m4dateformat.moment);
        });
    };

    // Sets table action column to be 60px across if buttons are present in it
    $('tr td:last-child button.btn:visible', target).each(function () {
        if ($(this).parents('table').find('th:last-child').is('.autowidth')) {
            return false;
        } else {
            if ($(this).siblings('button.btn:visible').length > 0) {
                $(this).parents('table').find('th:last-child').css('min-width', '60px');
            } else {
                if ($(this).parents('table').is('.gridtable')) {
                    $(this).parents('table').find('th:last-child').css('min-width', '60px');
                } else {
                    if ($(this).parents('table').is('#accomtable')) {
                        $(this).parents('table').find('th:last-child').css('min-width', '100px');
                    } else {
                        $(this).parents('table').find('th:last-child').css('min-width', '30px');
                    }
                }
            }
        }
    });
    $('.tablerowtoggles', target).on('change', function () {
        var table = $(this).parents('div.control-group').data('tabletotoggle');
        var target = $(this).data('toggle');
        $(this).parents('section').find('table#' + table + ' tr[data-toggle="' + target + '"]').toggle();
    });


    // Set up for responsive tables
    $('table:has(thead)', target).not('#itinery').each(function () {
        var thcount = $(this).find('th').length;
        for (var i = 1; i < thcount + 1; i++) {
            var thisid = $(this).attr("id");
            var headerdata = $("#" + thisid + " thead th:nth-child(" + i + ")").text();
            $("#" + thisid + " tbody tr td:nth-child(" + i + ")").attr('data-headerdetails', headerdata);
        }
    });

    //  For droppable tables, if all actual rows are deleted, then produce a placeholder drop location row.
    $("button.deleterow", target).on("click", function () {
        var thisid = $(this).parents("table").attr("id");
        var firetherest = "N";
        if ($(this).parents("table").is('.inrowedittable')) {
            firetherest = "Y";
        }
        $(this).parents('tr').remove();
        $('#backgroundPopup2').hide();
        if (firetherest === "Y") {
            var i = $("#" + thisid + " tbody tr:visible").length;
            if (i === 1) {
                $("table.inrowedittable tbody button.deleterow").hide();
            }
            reidrowinputs($('#' + thisid + '').parents('form'));
            $('#' + thisid + '').trigger('cleanandvalidate');
        }
        adjustscroll(target);
    });

    $('table.datatabled tfoot td.searchable', target).each(function () {
        var title = $(this).parents('table').find('thead th').eq($(this).index()).text();
        $(this).html('<input type="text" placeholder="Search ' + title + '" class="input-small columnfilter" />');
    });

    $('table.datatabled', target).not('.con-draggable').each(function () {
        var perpageprompt = "_MENU_ records per page";
        var filterprompt = "Filter returned records:";
        var totalprompt = "_TOTAL_ total records";
        var emptytableprompt = "Sorry, no records were found";
        if ($(this).is('[data-perpageprompt]')) {
            perpageprompt = "_MENU_" + $(this).data('perpageprompt');
        }
        if ($(this).is('[data-filterprompt]')) {
            filterprompt = $(this).data('filterprompt');
        }
        if ($(this).is('[data-totalprompt]')) {
            totalprompt = "_TOTAL_" + $(this).data('totalprompt');
        }
        if ($(this).is('[data-emptytableprompt]')) {
            emptytableprompt = $(this).data('emptytableprompt');
        }
        var lang = {
            "sLengthMenu": perpageprompt,
            "sSearch": filterprompt,
            "sInfo": totalprompt,
            "sInfoEmpty": emptytableprompt,
            "sEmptyTable": emptytableprompt
        };
        var defaultsortcol = $(this).find('thead th[data-defaultsort]');
        var updown = defaultsortcol.data("defaultsort");
        var defaultcolumnlocation = $($(this).find('thead th')).index(defaultsortcol);
        if (defaultsortcol.length < 1) {
            defaultcolumnlocation = 1;
            updown = "asc";
        }
        var columncount = $(this).find('thead th').length;
        var finalcolumncells = $(this).find('tbody tr td:nth-child(' + columncount + ')');
        var cellarray = [];
        var i = 0;
//        if ($(this).find('tbody td :input:disabled').not('button').length > 0) {
        for (i = 0; i < columncount; i++) {
            var focusedcell = $(this).find('tbody tr:first td')[i];
            if ($(focusedcell).find(':input').length < 1 || $(focusedcell).find('button, .btn').length > 0) {
                if ($(focusedcell).find('span[class*="date"], span[name*="date"]').length > 0) {
                    cellarray.push({
                        "orderDataType": "dom-datestatic"
                    });
                } else {
                    cellarray.push(null);
                }
            } else {
                var focusedcellinput = $(focusedcell).find(':input:visible:first');
                if (focusedcellinput.is('select')) {
                    cellarray.push({
                        "orderDataType": "dom-select"
                    });
                } else {
                    if (focusedcellinput.is('textarea')) {
                        cellarray.push({
                            "orderDataType": "dom-textarea"
                        });
                    } else {
                        if (focusedcellinput.is('.con-datepicker, .con-startdate, .con-enddate')) {
                            cellarray.push({
                                "orderDataType": "dom-datesedittable"
                            });
                        } else {
                            var focusedcellinputtype = $(focusedcell).find(':input:visible:first').attr('type');
                            switch (focusedcellinputtype) {
                                case 'checkbox':
                                    cellarray.push({
                                        "orderDataType": "dom-checkbox"
                                    });
                                    break;
                                case 'text':
                                    cellarray.push({
                                        "orderDataType": "dom-text"
                                    });
                                    break;
                                default:
                                    cellarray.push(null);
                                    break;
                            }
                        }
                    }
                }
            }
        }
//        }
//        else {
//            for (i = 0; i < columncount; i++) {
//                cellarray.push(null);
//            }
//        }
        if ($(this).find('thead th[data-nosort="Y"]').length > 0) {
            var nosortcolumn = $(this).find('thead th[data-nosort="Y"]');
            $.each(nosortcolumn, function () {
                var nosortcolumnindex = $($(this).parents('table').find('thead th')).index($(this));
                cellarray[nosortcolumnindex] = {
                    "bSortable": false
                };
            });
        }
        if ($(this).find('thead th[data-html="Y"]').length > 0) {
            var htmlcolumn = $(this).find('thead th[data-html="Y"]');
            $.each(htmlcolumn, function () {
                var htmlcolumnindex = $($(this).parents('table').find('thead th')).index($(this));
                cellarray[htmlcolumnindex] = {
                    "sType": "html"
                };
            });
        }
        if ($(this).find('thead th[data-numhtml="Y"]').length > 0) {
            var numhtmlcolumn = $(this).find('thead th[data-numhtml="Y"]');
            $.each(numhtmlcolumn, function () {
                var numhtmlcolumnindex = $($(this).parents('table').find('thead th')).index($(this));
                cellarray[numhtmlcolumnindex] = {
                    "sType": "num-html"
                };
            });
        }
        if ($(this).find('thead th[data-datesort="Y"]').length > 0) {
            var datecolumn = $(this).find('thead th[data-datesort="Y"]');
            $.each(datecolumn, function () {
                var datecolumnindex = $($(this).parents('table').find('thead th')).index($(this));
                cellarray[datecolumnindex] = {
                    "sType": "date-uk"
                };
            });
        }
        if ($(this).find('thead th[data-datasort="Y"]').length > 0) {
            var selectedheader = $(this).find('thead th[data-datasort="Y"]');
            $.each(selectedheader, function () {
                var selectedheaderindex = $($(this).parents('table').find('thead th')).index($(this));
                cellarray[selectedheaderindex] = {
                    "sType": "m4-data"
                };
            });
        }
        if (finalcolumncells.find('button.btn').length > 0) {
            cellarray[columncount - 1] = {
                "bSortable": false
            };
        }
        if ($(this).is('#emailstable')) {
            cellarray[0] = {
                "bSortable": false
            };
        }
        var mytable;
        var options = {
            "sPaginationType": "ellipses",
            "oLanguage": lang,
            "aaSorting": [[defaultcolumnlocation, updown]],
            "columns": cellarray,
            "aLengthMenu": [[10, 25, 50], [10, 25, 50]],
            "bAutoWidth": false
        };
        if ($(this).is('[data-pre-destroy]')) {
            options.bDestroy = true;
        }
        if ($(this).is('[data-fullscreen]')) {
            options.sDom = "<'row-fluid'<'pull-left fullscreenbuttonarea'><'pull-right'f>r><'tablewrapper't><'row-fluid toptweak'<'pull-left'l><'middle'i><'pull-right'p>>";
        }
        if ($(this).is('[data-datatablescroll]')) {
            options.scrollX = "100%";
        }
        mytable = $(this).dataTable(options);
        if ($(this).is('[data-fixright]') || $(this).is('[data-fixleft]')) {
            var fixright = 0;
            var fixleft = 0;
            if ($(this).data('fixright')) {
                fixright = parseInt($(this).data('fixright'));
            }
            if ($(this).data('fixleft')) {
                fixleft = parseInt($(this).data('fixleft'));
            }
            new $.fn.dataTable.FixedColumns(mytable, {
                "iRightColumns": fixright,
                "iLeftColumns": fixleft
            });
            mytable.DataTable().draw();
            mytable.api().fixedColumns().update();
            $(this).on('order.dt', function () {
                mytable.api().fixedColumns().update();
            });
        }
        $(this).parents('.dataTables_wrapper').on('keyup', 'input.columnfilter', function () {
            console.log('yay!');
            mytable.api()
                    .column($(this).parent().index() + ':visible')
                    .search(this.value)
                    .draw();
        });
        $(this).parents('.dataTables_wrapper').on('click', 'button.fullscreenbutton', function () {
            $(this).parents('.dataTables_wrapper').toggleClass('fullscreen');
            if ($(this).parents('.dataTables_wrapper').is('.fullscreen')) {
                $(this).empty().append('<i class="icon icon-resize-small icon-white"></i> Normal view');
            } else {
                $(this).empty().append('<i class="icon icon-resize-full icon-white"></i> Fullscreen view');
            }
            mytable.api().draw();
            if ($(this).is('[data-fixright]') || $(this).is('[data-fixleft]')) {
                mytable.api().fixedColumns().update();
            }
        });
        if ($(this).parents('.dataTables_wrapper').find('.fullscreenbuttonarea button').length < 1 && $(this).parents('.dataTables_wrapper').find('.fullscreenbuttonarea').length > 0) {
            $(this).parents('.dataTables_wrapper').find('.fullscreenbuttonarea').html('<button class="fullscreenbutton btn btn-info" type="button"><i class="icon icon-resize-full icon-white"></i> Fullscreen view</button>');
        }
        $(this).on('page.dt order.dt length.dt', function () {
            var that = $(this);
            if ($(this).data('editmode')) {
                if ($(this).data('editmode') === "Y") {
                    console.log('EDITMODE PAGED!');
                    $(this).one('draw.dt', function () {
                        editmode($(this));
                    });
                } else {
                    console.log('VIEWMODE PAGED!');
                    $(this).one('draw.dt', function () {
                        viewmode($(this));
                    });
                }
            } else {
                if ($(this).find('thead tr button.include').length > 0) {
                    if ($(this).find('thead tr button.include').is(':disabled')) {
                        console.log('VIEWMODE PAGED!');
                        that.one('draw.dt', function () {
                            viewmode(that);
                        });
                    } else {
                        console.log('EDITMODE PAGED!');
                        that.one('draw.dt', function () {
                            editmode(that);
                        });
                    }
                } else {
                    var parents = '';
                    var disabled = false;
                    var disabledbuttons = that.find('tbody td button.include:disabled');
                    var enabledbuttons = that.find('tbody td button.include:not(":disabled")');
                    if (disabledbuttons.length > enabledbuttons.length) {
                        disabled = true;
                    }
                    if (disabled) {
                        parents = that.find('tbody td:has(button.include:not(":disabled"))');
                        console.log('VIEWMODE PAGED!');
                        that.one('draw.dt', function () {
                            viewmode(that);
                            editmode(parents);
                            //Servicestable crappy fix
                            viewmode($('#servicestable tr.info'));
                        });
                    } else {
                        parents = that.find('tbody td:has(button.include:disabled)');
                        console.log('EDITMODE PAGED!');
                        that.one('draw.dt', function () {
                            editmode(that);
                            viewmode(parents);
                            //Servicestable crappy fix
                            viewmode($('#servicestable tr.info'));
                        });
                    }
                }
            }
        });
    });
}

var resizetimer;
var tomtest = function () {
    clearTimeout(resizetimer);
    resizetimer = setTimeout(function () {
        $('.dataTables_scrollBody .invoicetables').each(function () {
            $(this).dataTable().fnAdjustColumnSizing();
        });
        $('table[data-fixright][id]:not([id^="DataTables_Table_"]), table[data-fixleft][id]:not([id^="DataTables_Table_"])').each(function () {
            $(this).dataTable().api().draw("page");
            $(this).dataTable().api().fixedColumns().update();
        });
        $('table.ajax-table[id]:not([id^="DataTables_Table_"])').each(function () {
            $(this).DataTable().draw("page");
        });
        console.log('Resize adjust fired');
    }, 500);
};

$(window).off('resize.tomtest', tomtest).on('resize.tomtest', tomtest);

/* Default class modification */
$.extend($.fn.dataTableExt.oStdClasses, {
    "sWrapper": "dataTables_wrapper form-inline"
});

jQuery.extend(jQuery.fn.dataTableExt.oSort, {
    "num-html-pre": function (a) {
        var x = String(a).replace(/<[\s\S]*?>/g, "");
        return parseFloat(x);
    },
    "num-html-asc": function (a, b) {
        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
    },
    "num-html-desc": function (a, b) {
        return ((a < b) ? 1 : ((a > b) ? -1 : 0));
    }
});

$.extend($.fn.dataTableExt.oSort, {
    "title-string-pre": function (a) {
        return a.match(/title="(.*?)"/)[1].toLowerCase();
    },
    "title-string-asc": function (a, b) {
        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
    },
    "title-string-desc": function (a, b) {
        return ((a < b) ? 1 : ((a > b) ? -1 : 0));
    }
});

jQuery.extend(jQuery.fn.dataTableExt.oSort, {
    "m4-data-pre": function (a) {
        var x = a.match(/data-sort-value="*(-?[0-9\.]+)/)[1];
        return parseFloat(x);
    },
    "m4-data-asc": function (a, b) {
        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
    },
    "m4-data-desc": function (a, b) {
        return ((a < b) ? 1 : ((a > b) ? -1 : 0));
    }
});

$.extend($.fn.dataTableExt.oSort, {
    "date-uk-pre": function (a) {
        var b = $(a).text();
        var ukDatea = b.split('-');
        return (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
    },
    "date-uk-asc": function (a, b) {
        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
    },
    "date-uk-desc": function (a, b) {
        return ((a < b) ? 1 : ((a > b) ? -1 : 0));
    }
});

window.tomdata = {};

/**
 * Initialises pagination functionality for an ul, also generates suitable paging controls.
 * @method genericpaginate
 * @param {JSON Array} sortedlist Required parameter, suitably filtered and sorted results which you wish to paginate and display in your list.
 * @param {Number} pageno Required parameter, for initialisation should ALWAYS be 1.
 * @param {Jquery object} targettable Required parameter, should currently be a ul element, with a unique id.
 * @param {Array} noperpageoptions Optional array of values. If provided, these will form the options in the no.records/page select. If not provided, options will default to 20, 50, 100. Will accept 'All' as a parameter, which is only to be used if you know the total potential size of the data set and it is feasible to load and show that many results at once. If you don't know, don't use it.
 * @version
 * @author Tom Yeldham
 */

function genericpaginate(sortedlist, pageno, targettable, noperpageoptions) {
    var datapairvalue = targettable.attr('id');
    var targettablearrayname = datapairvalue + "sorted";
    window.tomdata[targettablearrayname] = sortedlist;
    var pagecontrols = targettable.next('.con-addresstablepaginationcontrols');
    var pairednoperpagecontrol = $('.con-addresstablerecordsperpage[data-datapair="' + datapairvalue + '"] select');
    if (noperpageoptions === undefined) {
        noperpageoptions = ['20', '50', '100'];
    }
    if (pageno === undefined) {
        pageno = parseInt(pagecontrols.find('.con-pagination li.active').data('pageno'));
    }
    if (pageno === "prev") {
        pageno = parseInt(pagecontrols.find('.con-pagination li.active').data('pageno')) - 1;
    }
    if (pageno === "next") {
        pageno = parseInt(pagecontrols.find('.con-pagination li.active').data('pageno')) + 1;
    } else {
        pageno = parseInt(pageno);
    }
    if (pairednoperpagecontrol.length !== 1) {
        if (pagecontrols.length !== 1) {
            targettable.after('<div class="con-addresstablepaginationcontrols">\n\
<div class="pull-left con-addresstablerecordsperpage" data-datapair="' + datapairvalue + '">\n\
<select class="input-mini">\n\
</select>\n\
<span class="help-inline">records per page</span>\n\
</div>\n\
<div class="pull-right pagination con-pagination" data-datapair="' + datapairvalue + '">\n\
<ul></ul>\n\
</div>\n\
</div>');
        } else {
            pagecontrols.prepend('<div class="pull-left con-addresstablerecordsperpage" data-datapair="' + datapairvalue + '">\n\
<select class="input-mini">\n\
</select>\n\
<span class="help-inline">records per page</span>\n\
</div>');
        }
        pairednoperpagecontrol = $('.con-addresstablerecordsperpage[data-datapair="' + datapairvalue + '"] select');
        pagecontrols = targettable.next('.con-addresstablepaginationcontrols');
        for (var i = 0; i < noperpageoptions.length; i++) {
            pairednoperpagecontrol.append('<option value="' + noperpageoptions[i] + '">' + noperpageoptions[i] + '</option>');
        }
        setuppagination(pagecontrols);
    }
    var totalnoofaddresses = sortedlist.length;
    var addressesperpage = pairednoperpagecontrol.val();
    if (addressesperpage === "All") {
        addressesperpage = totalnoofaddresses;
    }
    var noofpagesneeded = Math.ceil(totalnoofaddresses / addressesperpage);
    var startpoint = parseInt(addressesperpage) * (pageno - 1);
    var endpoint = startpoint + parseInt(addressesperpage);
    var thispageworthofaddresses = sortedlist.slice(startpoint, endpoint);
    var table = targettable.find('ul');
    table.empty();
    var tableid = targettable.attr('id');
    tablerender[tableid].render(thispageworthofaddresses, table);
    if (tablerender[tableid].additionalfunctioncalls) {
        tablerender[tableid].additionalfunctioncalls(targettable);
    }
    var paginationcontrol = $('.con-pagination[data-datapair="' + datapairvalue + '"] ul');
    paginationcontrol.empty();
    if (noofpagesneeded > 1) {
        paginationcontrol.append('<li data-pageno="prev"><a><</a></li>');
        if (noofpagesneeded <= 5) {
            for (var j = 1; j < noofpagesneeded + 1; j++) {
                paginationcontrol.append('<li data-pageno="' + j + '"><a>' + j + '</a></li>');
            }
        } else {
            var pagestomake = [];
            pagestomake.push(1, (pageno - 1), pageno, (pageno + 1), noofpagesneeded);
            var index1 = $.inArray(0, pagestomake);
            if (index1 !== -1) {
                pagestomake.splice(index1, 1);
            }
            var index2 = $.inArray(noofpagesneeded + 1, pagestomake);
            if (index2 !== -1) {
                pagestomake.splice(index2, 1);
            }
            pagestomake = $.grep(pagestomake, function (el, index) {
                return index === $.inArray(el, pagestomake);
            });
            for (var k = 0; k < pagestomake.length; k++) {
                paginationcontrol.append('<li data-pageno="' + pagestomake[k] + '"><a>' + pagestomake[k] + '</a></li>');
            }
            $.each(paginationcontrol.find('li'), function () {
                if (parseInt($(this).data("pageno")) + 1 !== parseInt($(this).next().data("pageno")) && $(this).next().length > 0 && $(this).data("pageno") !== "prev") {
                    $(this).after('<li data-pageno="..." class="disabled"><a>...</a></li>');
                }
            });
        }
        paginationcontrol.append('<li data-pageno="next"><a>></a></li>');
        paginationcontrol.find('li[data-pageno="' + pageno + '"]').addClass('active');
        if (paginationcontrol.find('li.active').next().is('li[data-pageno="next"]')) {
            paginationcontrol.find('li[data-pageno="next"]').addClass('disabled');
        } else {
            if (paginationcontrol.find('li.active').prev().is('li[data-pageno="prev"]')) {
                paginationcontrol.find('li[data-pageno="prev"]').addClass('disabled');
            }
        }
    }
}

/**
 * Initialises functionality on generated paging controls. Called automatically when needed.
 * @method setuppagination
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */

function setuppagination(target) {
    $('.con-pagination', target).on('click', 'ul li:not(".disabled, .active")', function (e) {
        var targettableid = $(this).parents('.con-pagination').data('datapair');
        var targettablearrayname = targettableid + "sorted";
        genericpaginate(window.tomdata[targettablearrayname], $(this).data('pageno'), $('#' + targettableid));
    });
    $('.con-addresstablerecordsperpage', target).on('change', 'select', function () {
        var targettableid = $(this).parents('.con-addresstablerecordsperpage').data('datapair');
        var targettablearrayname = targettableid + "sorted";
        genericpaginate(window.tomdata[targettablearrayname], 1, $('#' + targettableid));
    });
}


function bangingtables(table) {
    var debounce;
    var url = $(table).data('url');
    var defsortorder = 'desc';
    var defsortcol = 1;
    function getSelectedRows() {
        if (table.selectedrows && table.selectedrows[0]) {
            return table.selectedrows.join(",");
        }
    }
    function selectrow(rows, keepothers) {
        if (!table.selectedrows) {
            table.selectedrows = [];
        }
        if (!keepothers) {
            table.find('tr.selected').removeClass('selected');
            table.selectedrows = [];
        }
        if (Array.isArray(rows)) {
            var toselect = [];
            rows.forEach(function (row, i) {
                if (!$(row).is('.selected')) {
                    toselect.push(row);
                }
            });
            if (toselect[0]) {
                toselect.forEach(function (row, i) {
                    table.selectedrows.push($(row).attr('id'));
                    $(row).addClass('selected');
                });
            } else {
                rows.forEach(function (row, i) {
                    table.selectedrows = table.selectedrows.filter(function (value) {
                        return value !== $(row).attr('id');
                    });
                    $(row).removeClass('selected');
                });
            }

        } else {
            var id = $(rows).attr('id');
            $(rows).toggleClass('selected');
            if (!table.selectedrows.includes(id)) {
                table.selectedrows.push(id);
            } else {
                table.selectedrows = table.selectedrows.filter(function (value) {
                    return value !== $(rows).attr('id');
                });
            }
        }
        if (table.selectedrows.length > 1) {
            table.parents('.tablewrapper').find('.bigdaddyactiondropdown').prop('disabled', '');
        } else {
            table.parents('.tablewrapper').find('.bigdaddyactiondropdown').prop('disabled', 'disabled');
        }
    }

    function getColors(link) {
        if (link.bgcolor && link.fgcolor) {
            return 'style="background-color:' + link.bgcolor + '; color:' + link.fgcolor + '"';
        } else {
            return '';
        }
    }

    function getIcons(link) {
        if (link.icon) {
            return '<i class="' + link.icon + '"></i> ';
        } else {
            return '';
        }
    }

    function buildQuery(link, batch) {
        var action = link.action;
        if (action.indexOf('%RECORDS%') > -1) {
            if (batch) {
                action = action.replace('%RECORDS%', getSelectedRows());
            } else {
                action = action.replace('%RECORDS%', table.selectedrows[table.selectedrows.length - 1]);
            }
        }
        if (link.action.indexOf('%RECORD%') > -1) {
            action = action.replace('%RECORD%', table.selectedrows[table.selectedrows.length - 1]);
        }
        return action;
    }

    function addbaselinks(link, batch) {
        var str = '';
        var style = getColors(link);
        var icon = getIcons(link);
        if (link.type === 'divider') {
            str = '<li class="divider"></li>';
        } else {
            if (link.type === 'newtab') {
                str = '<li tabindex="0"><a  ' + style + ' tabindex="-1" href="#" data-linky="' + buildQuery(link, batch) + '">' + icon + link.title + '</a></li>';
            } else {
                if (link.type === 'redirect') {
                    str = '<li tabindex="0"><a class="redirect" ' + style + ' tabindex="-1" href="' + buildQuery(link, batch) + '">' + icon + link.title + '</a></li>';
                } else {
                    if (link.type === 'modal') {
                        str = '<li tabindex="0"><a  ' + style + ' tabindex="-1" href="' + buildQuery(link, batch) + '">' + icon + link.title + '</a></li>';
                    } else {
                        if (link.type === 'post') {
                            str = '<li tabindex="0"><a  ' + style + ' tabindex="-1" href="#" data-post="' + buildQuery(link, batch) + '" >' + icon + link.title + '</a></li>';
                        }
                    }
                }
            }
        }
        return str;
    }

    function getchildren(link, callback, batch) {
        $.ajax({
            url: $(link).find('a').data('remoteload'),
            success: function (data) {
                var str = '';
                data.forEach(function (sublink, x) {
                    str = str + addbaselinks(sublink, batch);
                });
                $(link).find('ul').append(str);
                callback(link);
            }
        });
    }

    function subactioncallback(that) {
        var subactionlist = $(that).find('ul');
        $(that).parents('ul').find('ul').hide();
        subactionlist.show().find('li:first-child').focus();
        //Temporary fix. Will work for remote loaded sub actions only. Luckily thats enough for controller table
        // If collision down
        var tableheight = $(that).parents('.tablewrapper').find('.dataTables_scrollBody').height();
        if ((tableheight - ($(that).parents('ul').position().top + $(that).position().top)) < subactionlist.height() + 5) {
            var cssrules = {};
            var vertspaceavailable = $(that).parents('ul').position().top + $(that).position().top - 5;
            //Check for Collision up
            if (vertspaceavailable > subactionlist.height()) {
                // If space just flip the list
                cssrules.top = 25 - subactionlist.height();
            }
            //If clashes the check to see if top of bottom has more space
            else {
                //If the top space is larger than half of the available table height
                if (vertspaceavailable > tableheight / 2) {
                    //Flip and set the max height to be the available space
                    cssrules["max-height"] = vertspaceavailable;
                    cssrules.top = 25 - vertspaceavailable;
                } else {
                    // Otherwise set the height to be remaining space below
                    cssrules["max-height"] = tableheight - vertspaceavailable;
                }
            }
            subactionlist.css(cssrules);
        }
    }

    function whipupadropdown(button, originbutton) {
        function generatelinks(triggerid) {
            var bigdaddyclassname = 'bigdaddyactionlist';
            var batch = false;
            if ($(button).is('.bigdaddyactiondropdown')) {
                bigdaddyclassname = 'bigdaddyactionlist';
                batch = true;
            }
            var linkstr = '<ul class="dropdown-menu brandnew ' + bigdaddyclassname + '" data-triggered="' + triggerid + '">';
            table.actions.forEach(function (normallink, i) {
                var thisstr = '';
                if (normallink.type === "submenu" || normallink.type === "submenu-load") {
                    var style = getColors(normallink);
                    var icon = getIcons(normallink);
                    if (normallink.type === "submenu") {
                        thisstr = thisstr + '<li class="dropdown-submenu" tabindex="0"><a  ' + style + ' tabindex="-1" >' + icon + normallink.title + '</a><ul class="dropdown-menu">';
                        normallink.actions.forEach(function (sublink, x) {
                            thisstr = thisstr + addbaselinks(sublink, batch);
                        });
                        thisstr = thisstr + '</ul></li>';
                    } else {
                        thisstr = thisstr + '<li class="dropdown-submenu" tabindex="0"><a  ' + style + ' tabindex="-1" data-remoteload="' + buildQuery(normallink, batch) + '">' + icon + normallink.title + '</a><ul class="dropdown-menu"></ul></li>';
                    }
                } else {
                    thisstr = thisstr + addbaselinks(normallink, batch);
                }
                linkstr = linkstr + thisstr;
            });
            return linkstr + '</ul>';
        }
        var row = $(table).find('tbody>tr')[$.inArray(button[0], $(button).parents('tbody').find('tr button.actiondropdown'))];
        if (originbutton) {
            row = $(table).find('tbody>tr')[$.inArray(originbutton[0], $(originbutton).parents('tbody').find('tr button.actiondropdown'))];
        }
        var boshoutanewone = true;
        if ($('.brandnew').length > 0) {
            if ($(row).attr('id') === String($('.brandnew').data('triggered'))) {
                boshoutanewone = false;
            }
            $('.brandnew').remove();
        }
        if (boshoutanewone) {
            table.parents('.tablewrapper').append(generatelinks($(row).attr('id')));
            var newdropdown = $('.brandnew');
            var left = $(button).position().left;
            var top = $(button).position().top + $(button).height() + 7;
            var batch = true;
            if (!$(button).is('.bigdaddyactiondropdown')) {
                top = top + table.parents('.dataTables_scroll').find('.dataTables_scrollHeadInner').height();
                batch = false;
            }
            if ($(table).parents('.dataTables_scrollBody').height() - top < $('.brandnew').height() + 5) {
                console.log('Clash detected');
                top = top - ($('.brandnew').height() + 9);
                left = left + $(button).width() + 14;
            }
            console.log($(button).position().top);
            newdropdown.css({top: top, left: left, display: "block"});
            $(newdropdown).on('mouseenter', 'li', function (e) {
                e.stopPropagation();
                if ($(this).parent('ul').is('.brandnew')) {
                    $(this).parents('ul').find('ul').hide();
                }
                if ($(this).is(':has(a[data-remoteload])')) {
                    if ($(this).find('ul > li').length < 1 && !$(this).is('.triggered')) {
                        $(this).addClass('triggered');
                        getchildren($(this), subactioncallback, batch);
                    } else {
                        subactioncallback($(this));
                    }
                } else {
                    if ($(this).find('ul').length > 0) {
                        $(this).find('ul').show().find('li:first-child').focus();
                    }
                }
            });
            $(newdropdown).find('li:first-child').focus();
        }
    }
    var refreshdebounce;
    function setDebounceTrue(table) {
        console.log('set/reset debounce');
        table.refreshdebounce = true;
        clearTimeout(refreshdebounce);
        refreshdebounce = setTimeout(function () {
            console.log('clear debounce');
            table.refreshdebounce = false;
        }, 10000);
    }
    var refresh;
    function setRefresh(rate, instarefresh, wasblocked) {
        clearInterval(refresh);
        if (instarefresh && $('.modal:visible').length < 1) {
            console.log('quick fired refresh');
            $(table).parents('.dataTables_wrapper').find('.ajaxtablefitlersearch').addClass('autocompletesearching');
            $(table).DataTable().ajax.reload(null, false);
            //on refresh rate
        }
        refresh = window.setInterval(function () {
            //remove the interval
            console.log('cleared refresh');
            // If action has occured in the last 10 seconds or dropdown is open
            if (table.refreshdebounce || $(table).parents('.tablewrapper').find('.brandnew').length > 0) {
                //Set this function to try again in 2 seconds
                setRefresh(2000, false, true);
            } else {
                var rate = parseInt($(table).parents('.dataTables_wrapper').find('.ajax-table-refresh').val()) * 1000;
                if (wasblocked) {
                    console.log('finally allowed to refresh');
                    $(table).parents('.dataTables_wrapper').find('.ajaxtablefitlersearch').addClass('autocompletesearching');
                    $(table).DataTable().ajax.reload(null, false);
                }
                //Otherwise reset this function to fire again based on the entered refresh rate and trigger an immediate refresh
                console.log('reset the refresh rate to' + parseInt($(table).parents('.dataTables_wrapper').find('.ajax-table-refresh').val()) * 1000);
                setRefresh(rate, true, false);
            }
        }, rate);
    }
    function startDebounce(e) {
        clearTimeout(debounce);
        console.log('clear search debounce');
        if (e.keyCode === '13') {
            setRefresh(parseInt($(table).parents('.dataTables_wrapper').find('.ajax-table-refresh').val()) * 1000, true, false);
            console.log('fired immediate search');
        } else {
            console.log('set search debounce');
            debounce = setTimeout(function () {
                console.log('fired debounced search');
                setRefresh(parseInt($(table).parents('.dataTables_wrapper').find('.ajax-table-refresh').val()) * 1000, true, false);
            }, 2500);
        }
    }
    function godoit(that, callback) {
        if (!callback) {
            callback = null;
        }
        var url = $(that).data('post').split('?')[0];
        var data = {};
        $(that).data('post').split('?')[1].split('&').forEach(function (chunk, i) {
            data[chunk.split('=')[0]] = chunk.split('=')[1];
        });
        $.ajax({
            url: url,
            method: "POST",
            data: data,
            success: function (data) {
                if (data.expression) {
                    return;
                }
                if (!data.success) {
                    var message = data.message;
                    if (!message)
                        message = "Problem in processing the request. Please contact system administrator.";
                    alert(message);
                } else {
                    if (data.reload) {
                        $(table).DataTable().ajax.reload(callback, false);
                    }
                }
            },
            error: function (jqXHR, textStatus) {
                alert('Errored due to: ' + textStatus);
            }
        });
    }
    $.ajax({
        url: url + '/actions',
        success: function (actions) {
            table.actions = actions;
            $.ajax({
                url: url + '/filters',
                success: function (filters) {
                    table.filters = filters;
                    $.ajax({
                        url: url + '/headers',
                        success: function (data) {
                            var coltableheadres = [];
                            $(table).append('<thead><tr></tr></thead><tbody></tbody><tfoot></tfoot>');
                            data.forEach(function (obj, i) {
                                if (!obj.hidden) {
                                    coltableheadres.push({"data": obj.title});
                                    $(table).find('thead tr').append('<th data-param="' + obj.name + '">' + obj.title + '</th>');
                                }
                            });
                            $(table).on('blur', 'tr', function (e) {
                                table.prevfocus = e.target;
                                console.log(table.prevfocus);
                            });
                            $(table).find('tbody').on('click', 'td', function (e) {
                                setDebounceTrue(table);
                                if (!$(e.target).is('.actiondropdown, i')) {
                                    $(e.target).parents('tr').focus();
                                    var focusedRow = $(document.activeElement);
                                    table.rowtoreselect = $(document.activeElement).attr('id');
                                    if (e.ctrlKey) {
                                        selectrow(focusedRow, true);
                                    } else {
                                        if (e.shiftKey) {
                                            var rows = $('tbody tr', table);
                                            var currenti = $.inArray(table.prevfocus, rows);
                                            var targeti = $.inArray($(e.target).parents('tr')[0], rows);
                                            if (currenti < targeti) {
                                                rows = rows.slice(currenti).splice(0, (targeti - currenti) + 1);
                                            } else {
                                                rows = rows.slice(targeti).splice(0, (currenti - targeti));
                                            }
                                            selectrow(rows, true);
                                        } else {
                                            selectrow(focusedRow, false);
                                        }
                                    }
                                }
                            }).on('click', '.actiondropdown, i', function (e) {
                                setDebounceTrue(table);
                                var targ = $(this);
                                if ($(this).is('i')) {
                                    targ = $(this).parents('.actiondropdown');
                                    e.stopPropagation();
                                }
                                var correspondingrow = $(table).find('tbody>tr')[$.inArray(targ[0], $(table).parents('.tablewrapper').find('.DTFC_LeftBodyLiner tbody>tr .actiondropdown'))];
                                selectrow(correspondingrow, false);
                                whipupadropdown(targ);
                                e.stopPropagation();
                            });
                            $(table).find('thead tr').prepend('<th><button class="btn bigdaddyactiondropdown" disabled data-toggle="tooltip" title="Batch action selected rows"><i class="icon icon-mini icon-align-justify"></i></button></th>');
                            coltableheadres.splice(0, 0, {"data": 'button'});
                            var viewheight = '75vh';
                            if ($(table).data('vh')) {
                                viewheight = $(table).data('vh');
                            }
                            $(table).DataTable({
                                serverSide: true,
                                processing: true,
                                scrollY: viewheight,
                                searchDelay: 2500,
                                ajax: {
                                    url: url,
                                    dataSrc: function (json) {
                                        var data = json.records.map(function (row) {
                                            var rowobj = {};
                                            row.values.forEach(function (obj, i) {
                                                if (obj === null) {
                                                    obj = '';
                                                }
                                                rowobj[json.headres[i].title] = obj;
                                            });
                                            rowobj.DT_RowAttr = {"style": "background-color:" + String(row.bgcolor.toLowerCase()) + "; color:" + String(row.fgcolor.toLowerCase())};
                                            rowobj.DT_RowId = row.id;
                                            return rowobj;
                                        });
                                        console.log(data);
                                        return data;
                                    },
                                    data: function (data, settings) {
                                        var sortcolumn = 1;
                                        var sortOrder = 'asc';
                                        if (table.DataTable().order()) {
                                            sortcolumn = table.DataTable().order()[0][0];
                                            sortOrder = table.DataTable().order()[0][1];
                                        }
                                        var obj = {
                                            offset: data.start,
                                            limit: data.length,
                                            hidText: $(table).parents('.dataTables_wrapper').find('.dataTables_filter input[type="search"]').val(),
                                            sendTotal: 'true',
                                            orderBy: $(table.parents('.tablewrapper').find('thead th')[sortcolumn]).data('param') + ' ' + sortOrder
                                        };
                                        if (table.filters[0]) {
                                            var filterId = table.filters[0].filterId;
                                            if ($(table).parents('.dataTables_wrapper').find('.ajax-table-filters').length > 0) {
                                                filterId = $(table).parents('.dataTables_wrapper').find('.ajax-table-filters').val();
                                            }
                                            obj.filterId = filterId;
                                        }
                                        return obj;
                                    },
                                    dataFilter: function (data) {
                                        var json = jQuery.parseJSON(data);
                                        json.recordsFiltered = json.recordsTotal;
                                        return JSON.stringify(json); // return JSON string
                                    }
                                },
                                fixedColumns: {
                                    leftColumns: 1,
                                    heightMatch: 'auto'
                                },
                                columns: coltableheadres,
                                columnDefs: [
                                    {targets: 0, defaultContent: '<button class="btn actiondropdown" data-toggle="tooltip" title="Action this row"><i class="icon icon-mini icon-align-justify"></i></button>', orderable: false},
                                    {targets: '_all', defaultContent: ""}
                                ],
                                sPaginationType: "ellipses",
                                aaSorting: [[1, 'asc']],
                                scrollX: "100%",
                                "sDom": "<'row-fluid'<'pull-left'l><'pull-right'f>r><'tablewrapper ajax-tablewrapper't><'row-fluid toptweak'<'pull-left'i><'pull-right'p>>",
                                createdRow: function (row, data, dataIndex) {
                                    $(row).attr('tabindex', '0');
                                },
                                "initComplete": function (settings, json) {
                                    $(table).parents('.dataTables_wrapper').find('.dataTables_filter input[type="search"]').off('keyup.DT input.DT').on('keydown', function (e) {
                                        startDebounce(e);
                                    });
                                    $(table).parents('.dataTables_wrapper').find('.dataTables_filter input[type="search"]').after('<span class="add-on"><i class="icon-search ajaxtablefitlersearch"></i></span></div>');
                                    $(table).parents('.dataTables_wrapper').find('.dataTables_filter input[type="search"]').before('<div class="input-append">');
                                    var needsametaboot = false;
                                    if ($(table).parents('.dataTables_wrapper').find('.ajax-table-filters').length < 1 && table.filters[0]) {
                                        var filteroptions = '';
                                        table.filters.forEach(function (filter, i) {
                                            filteroptions = filteroptions + '<option value="' + filter.filterId + '">' + filter.title + '</option>';
                                        });
                                        $(table).parents('.dataTables_wrapper').find('.dataTables_filter').append('<label>Filters:<select class="ajax-table-filters input-large">' + filteroptions + '</select>&nbsp;&nbsp;</label>');
                                        $(table).parents('.dataTables_wrapper').on('change', '.ajax-table-filters', function () {
                                            $(table).DataTable().ajax.reload();
                                        });
                                        needsametaboot = true;
                                    }
                                    if ($(table).parents('.dataTables_wrapper').find('.ajax-table-refresh').length < 1) {
                                        var rate = 20;
                                        if ($(table).data('rate')) {
                                            rate = $(table).data('rate');
                                        }
                                        $(table).parents('.dataTables_wrapper').find('.dataTables_filter').append('<div class="input-append ajax-table-refresh-wrapper"><input type="text" class="ajax-table-refresh input-mini" value="' + rate + '" data-oldrate="' + rate + '"/><button class="btn refreshbutton" type="button"><i class="icon icon-refresh"></i></button></div>');
                                        $(table).parents('.dataTables_wrapper').on('focus', '.ajax-table-refresh', function () {
                                            window.clearInterval(refresh);
                                        });
                                        $(table).parents('.dataTables_wrapper').on('blur', '.ajax-table-refresh', function () {
                                            if (parseInt($(this).val()) < 20) {
                                                $(this).val('20');
                                            }
                                            window.clearInterval(refresh);
                                            setRefresh(parseInt($(this).val()) * 1000, true, false);
                                        });
                                        $(table).parents('.dataTables_wrapper').on('click', '.refreshbutton', function () {
                                            window.clearInterval(refresh);
                                            setRefresh(parseInt($(table).parents('.dataTables_wrapper').find('.ajax-table-refresh').val()) * 1000, true, false);
                                        });
                                        setRefresh(parseInt(rate * 1000), false, false);
                                        needsametaboot = true;
                                    }
                                    if ($(table).parents('.dataTables_wrapper').find('.ajax-table-legend').length < 1) {
                                        $(table).parents('.dataTables_wrapper').find('.dataTables_length').prepend('<button class="ajax-table-legend btn" title="Click to see keyboard commands" data-toggle="modal" data-target="#' + $(table).attr('id') + "legend" + '"><i class="fa fa-question-circle"></i></button>');
                                        $('body').append('\
<div class="modal nodisplay" id="' + $(table).attr('id') + "legend" + '" tabindex="-1" role="dialog">\n\
    <div class="modal-header">\n\
        <h3>Available Keyboard commands</h3>\n\
    </div>\n\
    <form class="form-horizontal">\n\
        <div class="modal-body row">\n\
            <div class="span6">\n\
                <fieldset>\n\
                    <div class="control-group">\n\
                        <label for="staticfield1" class="control-label">Space:</label>\n\
                        <div class="controls">\n\
                             <span>In table - Toggles selection on the currently focussed row.<br> \n\
In action/batch action lists - Triggers the currently focussed action on the selected row/rows.</span>\n\
                        </div>\n\
                    </div>\n\
                   <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Up arrow:</label>\n\
                        <div class="controls">\n\
                            <span>In table - Shifts focus to previous row. If on the first row of a page, loads previous page and focuses last row. <br> \n\
In action list - Shifts focus to previous action.</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Down arrow:</label>\n\
                        <div class="controls">\n\
                            <span>In table - Shifts focus to next row. If on the last row of a page, loads next page and focuses first row. <br> \n\
In action list - Shifts focus to next action.</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Left arrow:</label>\n\
                        <div class="controls">\n\
                            <span>In table - Scrolls the table to the left.<br> \n\
In action/batch action list - Closes sub action menu and shifts focus to original action. If focussed on action link (top level, closes action menu and returns focus to the selected row.</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Right arrow:</label>\n\
                        <div class="controls">\n\
                            <span>In table - Scrolls the table to the right.<br> \n\
In action/batch action list - Opens sub action menu and focuses 1st action within.</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Ctrl + Up arrow:</label>\n\
                        <div class="controls">\n\
                            <span>Select current row, if not already selected, Select and focus the previous row. If on 1st row of a page, go to previous page if possible and focus the last row. If both currently focussed and previous row are already selected, then deselect the current row and focus the previous row</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Ctrl + Down arrow:</label>\n\
                        <div class="controls">\n\
                            <span>Select current row, if not already selected, Select and focus the next row. If on 1st row of a page, go to next page if possible and focus the first row. If both currently focussed and next row are already selected, then deselect the current row and focus the next row</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Ctrl + Right arrow:</label>\n\
                        <div class="controls">\n\
                            <span>Load next page of results. Shift focus to 1st row of new page.</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Ctrl + Left arrow:</label>\n\
                        <div class="controls">\n\
                            <span>Load previous page of results. Shift focus to 1st row of new page.</span>\n\
                        </div>\n\
                    </div>\n\
                </fieldset>\n\
            </div>\n\
            <div class="span6">\n\
                <fieldset>\n\
                    <div class="control-group">\n\
                        <label for="staticfield1" class="control-label">Ctrl + R:</label>\n\
                        <div class="controls">\n\
                            <span>Refresh the table. </span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield1" class="control-label">Ctrl + F:</label>\n\
                        <div class="controls">\n\
                            <span>Shift focus to the filter. If currently in filter, shifts focus back into the table.</span>\n\
                        </div>\n\
                    </div>\n\
                   <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Enter:</label>\n\
                        <div class="controls">\n\
                            <span>In table - Opens action menu for the selected row. Shifts focus to the 1st action in the menu.<br> \n\
In action/batch action list - Closes action menu. Shifts focus to the last selected row</span>\n\
                        </div>\n\
                    </div>\n\
                   <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Ctrl + Enter:</label>\n\
                        <div class="controls">\n\
                            <span>In table - Opens batch action menu for the selected rows. Shifts focus to the 1st action in the menu.<br> \n\
In action/batch action list - Closes batch action menu. Shifts focus to the last selected row</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Escape:</label>\n\
                        <div class="controls">\n\
                            <span>Close action menu. Focus the last selected row</span>\n\
                        </div>\n\
                    </div>\n\
                </fieldset>\n\
                <fieldset>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Click:</label>\n\
                        <div class="controls">\n\
                            <span>Select & focus this row. Deselect other selected rows.</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Ctrl + Click:</label>\n\
                        <div class="controls">\n\
                            <span>Select and focus this row.</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">Shift + Click:</label>\n\
                        <div class="controls">\n\
                            <span>Select all rows between the currently focussed row and this one. Shift focus to this row. If any of the rows are unselected, then select them all. If they are all selected, deselect them all.</span>\n\
                        </div>\n\
                    </div>\n\
                    <div class="control-group">\n\
                        <label for="staticfield2" class="control-label">F1:</label>\n\
                        <div class="controls">\n\
                            <span>Opens assign driver action for the selected row. Auto focus on driver search. After select a driver will auto focus on assign button.</span>\n\
                         </div>\n\
                    </div>\n\
                </fieldset>\n\
            </div>\n\
        </div>\n\
        <div class="modal-footer">\n\
            <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>\n\
        </div>\n\
    </form>\n\
</div>');
                                        needsametaboot = true;
                                    }
                                    if (needsametaboot) {
                                        metaboot($(table).parents('.dataTables_wrapper').find('.dataTables_filter, .dataTables_length'));
                                        metaboot($('#' + $(table).attr('id') + 'legend'));
                                        $('#' + $(table).attr('id') + 'legend .modal-body').trigger('adjustmodalbodyminheight');
                                    }
                                }
                            });
                            $(table).on('draw.dt', function () {
                                if ($(table).parents('.tablewrapper').find('.brandnew').length > 0) {
                                    var callback = function (sodone) {
                                        $(table).parents('.tablewrapper').find('tbody tr[id="' + sodone + '"]').focus();
                                        $(table).parents('.tablewrapper').find('.brandnew').remove();
                                    };
                                    $(table).DataTable().ajax.reload(callback($(table).parents('.tablewrapper').find('.brandnew').data('triggered')), false);
                                }
                                if (table.selectedrows) {
                                    $.each($(table).find('tbody>tr'), function (i, row) {
                                        if (table.selectedrows.includes($(row).attr('id'))) {
                                            $(row).addClass('selected');
                                        }
                                    });
                                }
                                if (table.rowtoselect) {
                                    $('tbody>tr:' + table.rowtoselect, table).focus();
                                    table.rowtoselect = null;
                                } else {
                                    if (table.rowtoreselect) {
                                        $('tbody>tr#' + table.rowtoreselect, table).focus();
                                    } else {
                                        $('tbody>tr:first-child', table).focus();
                                    }
                                }
                                fired = false;
                                $(table).parents('.dataTables_wrapper').find('.ajaxtablefitlersearch').removeClass('autocompletesearching');
                            });
                            $(table).parents('.tablewrapper').on('click', '.brandnew [data-linky]', function (e) {
                                console.log('opening ' + $(this).data('linky'));
                                window.open($(this).data('linky'), "_blank");
                                e.preventDefault();
                            }).on('click', '.brandnew [data-post]', function (e) {
                                function refocusrow() {
                                    $(table).parents('.tablewrapper').find('tbody tr[id="' + $(document.activeElement).parents('.brandnew').data('triggered') + '"]').focus();
                                    table.rowtoreselect = $(document.activeElement).attr('id');
                                    $('.brandnew').remove();
                                }
                                godoit($(this), refocusrow);
                                e.preventDefault();
                            }).on('click', '.bigdaddyactiondropdown', function (e) {
                                if (!$(this).is(':disabled') && $('.brandnew').length < 1) {
                                    whipupadropdown($(this));
                                    e.stopPropagation();
                                } else {
                                    $('.brandnew').remove();
                                }
                            });
                            $(table).parents('.dataTables_wrapper').on('keydown', '.dataTables_filter input[type="search"]', function (e) {
                                if (e.keyCode === 70 && e.ctrlKey || e.keyCode === 27) {
                                    $('tbody>tr:first-child', table).focus();
                                    table.rowtoreselect = $(document.activeElement).attr('id');
                                    e.preventDefault();
                                }
                            });
                            var fired = false;
                            $(table).on('keydown', 'tr', function (e) {
                                setDebounceTrue(table);
                                console.log('table: ' + e.keyCode);
                                var focusedRow = $(document.activeElement);
                                table.rowtoreselect = $(document.activeElement).attr('id');
                                if (e.keyCode === 13) {
                                    if (!focusedRow.is('.selected')) {
                                        selectrow(focusedRow, false);
                                    }
                                    if (e.ctrlKey && table.selectedrows && table.selectedrows.length > 1) {
                                        whipupadropdown($(table).parents('.tablewrapper').find('.bigdaddyactiondropdown'), $($(table).parents('.tablewrapper').find('.DTFC_LeftBodyLiner tbody>tr .actiondropdown')[$.inArray(focusedRow[0], $(table).find('tbody>tr'))]));
                                    } else {
                                        whipupadropdown($($(table).parents('.tablewrapper').find('.DTFC_LeftBodyLiner tbody>tr .actiondropdown')[$.inArray(focusedRow[0], $(table).find('tbody>tr'))]));
                                    }
                                }
                                if (e.keyCode === 32) {
                                    selectrow(focusedRow, true);
                                    e.preventDefault();
                                }
                                if (e.keyCode === 70 && e.ctrlKey) {
                                    $(table).parents('.dataTables_wrapper').find('.dataTables_filter input[type="search"]').focus();
                                    e.preventDefault();
                                }
                                if (e.keyCode === 82 && e.ctrlKey) {
                                    $(table).parents('.dataTables_wrapper').find('.refreshbutton').trigger('click');
                                    e.preventDefault();
                                }
                                if (e.keyCode === 40 || e.keyCode === 38) {
                                    e.preventDefault();
                                    if (!fired) {
                                        if (!focusedRow.is('tbody>tr', table)) {
                                            $('tbody>tr:nth-child(1)', table).focus();
                                        } else
                                            fired = true;
                                        if (e.keyCode === 38) {
                                            if (focusedRow.is(':first-child') && $(table).DataTable().page() !== 0) {
                                                table.rowtoselect = 'last-child';
                                                $(table).DataTable().page('previous').draw(false);
                                            } else {
                                                if (e.ctrlKey) {
                                                    if (focusedRow.is('.selected') && focusedRow.prev('tr').is('.selected')) {
                                                        selectrow(focusedRow, true);
                                                    } else {
                                                        selectrow([focusedRow, focusedRow.prev('tr')], true);
                                                    }
                                                }
                                                focusedRow.prev('tr').focus();
                                                table.rowtoreselect = $(document.activeElement).attr('id');
                                                fired = false;
                                            }
                                        } else if (e.keyCode === 40) {
                                            if (focusedRow.is(':last-child') && $(table).DataTable().page.info().pages !== $(table).DataTable().page()) {
                                                table.rowtoselect = 'first-child';
                                                $(table).DataTable().page('next').draw(false);
                                            } else {
                                                if (e.ctrlKey) {
                                                    if (focusedRow.is('.selected') && focusedRow.next('tr').is('.selected')) {
                                                        selectrow(focusedRow, true);
                                                    } else {
                                                        selectrow([focusedRow, focusedRow.next('tr')], true);
                                                    }
                                                }
                                                focusedRow.next('tr').focus();
                                                table.rowtoreselect = $(document.activeElement).attr('id');
                                                fired = false;
                                            }
                                        }

                                    }
                                } else if (e.ctrlKey && e.which === 37 && $(table).DataTable().page() !== 0) {
                                    table.rowtoselect = 'first-child';
                                    $(table).DataTable().page('previous').draw(false);
                                } else if (e.ctrlKey && e.which === 39 && $(table).DataTable().page.info().pages !== $(table).DataTable().page()) {
                                    table.rowtoselect = 'first-child';
                                    $(table).DataTable().page('next').draw(false);
                                }
                            });
                            $(table).parents('.tablewrapper').on('keydown', '.brandnew li', function (e) {
                                setDebounceTrue(table);
                                console.log('list: ' + e.keyCode);
                                var focusedlink = $(document.activeElement);
                                var batch = false;
                                if (focusedlink.parents('ul').is('.bigdaddyactionlist')) {
                                    batch = true;
                                }
                                var callback = function () {
                                    focusedlink.find('ul').show().find('li:first-child').focus();
                                    e.stopPropagation();
                                };
                                function refocusrow() {
                                    $(table).parents('.tablewrapper').find('tbody tr[id="' + focusedlink.parents('.brandnew').data('triggered') + '"]').focus();
                                    $('.brandnew').remove();
                                }
                                if (e.keyCode === 13 || e.keyCode === 27) {
                                    refocusrow();
                                }
                                if (e.keyCode === 32) {
                                    e.preventDefault();
                                    e.stopPropagation();
                                    if (focusedlink.find('ul').length > 0) {
                                        if (focusedlink.find('ul > li').length < 1) {
                                            getchildren(focusedlink, subactioncallback, batch);
                                        } else {
                                            callback();
                                        }
                                    } else {
                                        if (focusedlink.find('[data-linky]').length > 0) {
                                            window.open(focusedlink.find('[data-linky]').data('linky'), "_blank");
                                        } else {
                                            if (focusedlink.find('[data-post]').length > 0) {
                                                godoit(focusedlink.find('[data-post]'), refocusrow);
                                            } else {
                                                if ($(focusedlink).find('.redirect').length > 0) {
                                                    window.location = focusedlink.find('a').attr('href');
                                                } else {
                                                    focusedlink.find('a').trigger('click');
                                                }
                                            }
                                        }
                                        refocusrow();
                                    }
                                }
                                if (e.keyCode === 70 && e.ctrlKey) {
                                    refocusrow();
                                    $(table).parents('.dataTables_wrapper').find('.dataTables_filter input[type="search"]').focus();
                                    e.preventDefault();
                                }
                                if (e.keyCode === 40 || e.keyCode === 38) {
                                    e.preventDefault();
                                    if (!focusedlink.is('li', $('.brandnew'))) {
                                        $('li:first-child', $('.brandnew')).focus();
                                    } else if (e.keyCode === 38) {
                                        if (!focusedlink.is(':first-child')) {
                                            focusedlink.prevAll('li:has("a")').first().focus();
                                        }
                                    } else if (e.keyCode === 40) {
                                        if (!focusedlink.is(':last-child')) {
                                            focusedlink.nextAll('li:has("a")').first().focus();
                                        }
                                    }
                                    e.stopPropagation();
                                } else if (e.which === 37) {
                                    if (!focusedlink.parents('ul:first').is('.brandnew')) {
                                        focusedlink.parents('li:first').focus();
                                        focusedlink.parents('ul:first').hide();
                                        e.stopPropagation();
                                    } else {
                                        refocusrow();
                                    }
                                } else if (e.which === 39 && focusedlink.find('ul').length > 0) {
                                    if (focusedlink.find('ul > li').length < 1) {
                                        getchildren(focusedlink, subactioncallback, batch);
                                        e.stopPropagation();
                                    } else {
                                        callback();
                                    }
                                }
                            });
                            var closajaxtablepopups = function () {
                                $('.brandnew').remove();
                            };
                            $('body').off('click', '', closajaxtablepopups).on('click', '', closajaxtablepopups);
                        }
                    });
                }
            });
        }
    });
}


/* $id$ */
/**
 * @class Uploaders
 */

/**
 * Initialises all uploader elements on the page
 * @method uploaderInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */

function uploaderInitialisation(target) {
    $('.fileupload', target).each(function() {
        $(this).fileupload({
            dropZone: $(this),
            autoUpload: true,
            sequentialUploads: true
        }).bind('fileuploadfailed', function(e, data) {
            bindremoveuploaditem($(this).find('tr.template-download:has("span.label-important")').not('.metabooted'));
            $(this).find('tr.template-download').addClass('metabooted');
        }).bind('fileuploadcompleted', function(e, data) {
            uploaderhiddenfields($(this));
        });
    });

    $(document).bind('drop dragover', function(e) {
        e.preventDefault();
    });

    function bindremoveuploaditem(target) {
        $('.deleteuploaditem', target).on('click', function() {
            var parentform = $(this).parents('form');
            if ($(this).parents('tr.template-download').find('span.label-important').length > 0) {
                $(this).parents('tr.template-download').remove();
                uploaderhiddenfields(parentform);
            }
        });
    }
}

/**
 * Deletes existing hidden fields for the successful uploads, then generates a hidden field for each successful upload for the uploader and sets it's value to the text content of the respective upload.
 * @method uploaderhiddenfields
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */

function uploaderhiddenfields(target) {
    var parentelement = target.parents('section');
    if(target.parents('div.modal')){
        parentelement = target.parents('div.modal');
    }
    var uploaderlogtemplate = parentelement.find('.fileuploaderlogtemplate');
    parentelement.find('.fileuploaderlog').remove();
    var successfuluploads = target.find('tr.template-download').not(':has("span.label-important")');
    var counter = 0;
    $.each(successfuluploads, function() {
        var uploadname = $(this).text();
        var strippeduploadname = uploadname.replace(/\s/g, "");
        uploaderlogtemplate.clone().addClass('newfileuploaderlog').removeClass('fileuploaderlogtemplate').attr('name', 'upload[' + counter + ']').after(uploaderlogtemplate);
        var newuploadlog = parentelement.find('.newfileuploaderlog');
        newuploadlog.val(strippeduploadname).addClass('fileuploaderlog').removeClass('newfileuploaderlog');
        counter++;
    });
}


/* $id$ */
/**
 * @class Utils
 */
//prevent console functions in broswers that don't have it
(function () {
    var method;
    var noop = function () {
    };
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }
}());

//function check loaded version of jquery against a number
(function ($) {
    $.versioncompare = function (version1, version2) {
        if ('undefined' === typeof version1) {
            throw new Error("We need at least 1 version");
        }
        version2 = version2 || $.fn.jquery;
        if (version1 == version2) {
            return 0;
        }
        var v1 = normalize(version1);
        var v2 = normalize(version2);
        var len = Math.max(v1.length, v2.length);
        for (var i = 0; i < len; i++) {
            v1[i] = v1[i] || 0;
            v2[i] = v2[i] || 0;
            if (v1[i] == v2[i]) {
                continue;
            }
            return v1[i] > v2[i] ? 1 : -1;
        }
        return 0;
    };
    function normalize(version) {
        return $.map(version.split('.'), function (value) {
            return parseInt(value, 10);
        });
    }
}(jQuery));

// A basic debounce.
function debounce(fn, delay) {
    var timer = null;
    return function () {
        var context = this, args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn.apply(context, args);
        }, delay);
    };
}

/**
 * Binds the click event of any .alertremoval elements to remove the elements parent div.alert. Used for close buttons in dynamically generated alert boxes
 * @method alertremoval
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */
function alertremoval(target) {
    $('.alertremoval', target).on('click', function () {
        $(this).parents('div.alert').remove();
    });
}

/**
 * Disables any wysiwyg editors and hides their respective toolbars.
 * @method disablerte
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */
function disablerte(target) {
    $('ul.wysihtml5-toolbar', target).parent().find("iframe").contents().find('body.wysihtml5-editor').attr('contenteditable', 'false');
    $('ul.wysihtml5-toolbar', target).parent().find("iframe").addClass('disabled');
    $('ul.wysihtml5-toolbar', target).hide();
}

/**
 * Enables any wysiwyg editors and shows their respective toolbars.
 * @method enablerte
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */
function enablerte(target) {
    $('ul.wysihtml5-toolbar', target).parent().find("iframe").contents().find('body.wysihtml5-editor').attr('contenteditable', 'true');
    $('ul.wysihtml5-toolbar', target).parent().find("iframe").removeClass('disabled');
    $('ul.wysihtml5-toolbar', target).show();
}

/**
 * Extend jquery load to included a loaded event
 * @method extendqjueryload
 * @version
 */

(function () {
    $.fn.jqueryLoad = $.fn.load;

    $.fn.load = function (url, params, callback) {
        var $this = $(this);
        var cb = $.isFunction(params) ? params : callback || $.noop;
        var wrapped = function (responseText, textStatus, XMLHttpRequest) {
            cb(responseText, textStatus, XMLHttpRequest);
            $this.trigger('loaded');
        };

        if ($.isFunction(params)) {
            params = wrapped;
        } else {
            callback = wrapped;
        }

        $this.jqueryLoad(url, params, callback);

        return this;
    };
})();

/**
 * Takes a string and capitalises the first character.
 * @method capitaliseFirstLetter
 * @param {object} string Required parameter. The string which you wish to capitalise.
 * @version
 * @author Tom Yeldham
 */
function capitaliseFirstLetter(string)
{
    return string.charAt(0).toUpperCase() + string.slice(1);
}


/**
 * Initialises the language settings for both the datepicker plugin and moment.js.
 * @method setdatepickers
 * @param {object} m4language Required global variable from screen specific config.js. Both the datepicker plugin and moment.js have seperate language formats that are loaded for each plugin respectively from the config file.
 * @version
 * @author Tom Yeldham
 */
(function ($) {
    $.fn.datepicker.dates.m4 = m4language.datepickers;
    moment.lang('m4', m4language.moment);
}(jQuery));

/**
 * Used to check passenger age for prepopulating booking modal page 1's using the data provided in the passenger table.
 * @method getAge
 * @param {object} dateString Required string. function splits the string at - delimiters and caluclates the age based on the current date.
 * @version
 * @author Tom Yeldham
 */
function getAge(dateString) {

    var dates = dateString.split("-");
    var d = new Date();

    var userday = dates[0];
    var usermonth = dates[1];
    var useryear = dates[2];

    var curday = d.getDate();
    var curmonth = d.getMonth() + 1;
    var curyear = d.getFullYear();

    var age = curyear - useryear;

    if ((curmonth < usermonth) || ((curmonth == usermonth) && curday < userday)) {

        age--;

    }

    return age;
}

/**
 * Enables form elements and shows various other elements within a target area. Also shows viewmode buttons and hides editmode buttons. Called on contents that need to be edittable straight away or to toggle off the effects for viewmode()
 * @method editmode
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @param {string} param2 Optional parameter additionally show elements with a certain class.
 * @return false
 * @version
 * @author Tom Yeldham
 */

function editmode(target, param2) {
    // Hides navbar editmode buttons, shows selected navbar buttons for this mode
    $('li', target).has('.editmode, .hidebuttons').hide();
    $('li', target).has('.' + param2).show();
    $('.available, a.remove-all, .action, .ui-datepicker-trigger, a.help, .hiddentable, button.underlay, .draghandlecontainer, .draghandle', target).show();
    $('button.edittable, button.addbutton, .include, .notesbutton, .ui-datepicker-trigger, button.underlay, li', target).attr('disabled', false);
    $('div.styled-select', target).has('select.include').not('table.table div.styled-select').removeClass('disabled');
    $('div.controls:has(input[type="radio"].include), div.controls:has(input[type="checkbox"].include)', target).removeClass('disabledradioorcheckbox');
    $('button.ui-multiselect', target).not('table.table button.ui-multiselect').attr('disabled', false);
    $('.archiveError').removeClass('archiveError');
    // Shows the add-ons (ie icons on the end of, various fields
    enablerte(target);
    sortoutaddons(target);
    $('.fileupload', target).fileupload('enable');
    adjustscroll(target);
    return false;
}

/**
 * Disables form elements and hides various other elements within a target area. Also hides viewmode buttons and shows editmode buttons. Usually called on load of a new record or to toggle off the effects for editmode()
 * @method viewmode
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @return false
 * @version
 * @author Tom Yeldham
 */

function viewmode(target) {
    $('.available, a.remove-all, .action, a.help, .hiddentable, button.underlay, .draghandlecontainer, .draghandle', target).hide();
    $('li', target).has('a.editmode, a.hidebuttons').show();
    $('li', target).has('a.viewmode, a.submit').hide();
    $('button.edittable, button.addbutton, .include, .notesbutton, button.underlay, button.ui-multiselect', target).attr('disabled', true);
    $('div.styled-select', target).has('select.include').addClass('disabled');
    $('div.controls:has(input[type="radio"].include), div.controls:has(input[type="checkbox"].include)', target).addClass('disabledradioorcheckbox');
    $('button.underlay', target).has('div.largeservice').show();
    $('#mainformsubmitlog').remove();
    disablerte(target);
    sortoutaddons(target);
    $('.fileupload', target).fileupload('disable');
    adjustscroll(target);
    return false;
}

function edittable(that, target) {
    var row = that.parents('tr');
    $('button.edittable, .include, button.addbutton').attr('disabled', true);
    row.find(':input').attr('disabled', false);
    $('div.styled-select').has('select.include').addClass('disabled');
    row.find('div.styled-select').has('select').removeClass('disabled');
    $('div.controls:has(input[type="radio"].include), div.controls:has(input[type="checkbox"].include)').addClass('disabledradioorcheckbox');
    row.find('div.controls:has(input[type="radio"].include), div.controls:has(input[type="checkbox"].include)').removeClass('disabledradioorcheckbox');
    row.find(":input:visible:first").focus();
    row.find("button").not('.hideuntiltablecollapse, .ui-multiselect').toggle();
    $('.editmode, .viewmode').addClass('disabled');
    multiselectfix(target);
    sortoutaddons();
    return false;
}

function viewtable(that, target) {
    var row = that.parents('tr');
    row.find('.error').removeClass('error');
    row.find('div.help-block').remove();
    row.find("button").not('.hideuntiltablecollapse, .ui-multiselect').toggle();
    row.find(':input').attr('disabled', true);
    $('div.styled-select').has('select.include').not('table.table div.styled-select').removeClass('disabled');
    row.find('div.styled-select').has('select').addClass('disabled');
    $('div.controls:has(input[type="radio"].include), div.controls:has(input[type="checkbox"].include)').removeClass('disabledradioorcheckbox');
    row.find('div.controls:has(input[type="radio"].include), div.controls:has(input[type="checkbox"].include)').addClass('disabledradioorcheckbox');
    $('button.edittable, .include, button.addbutton').attr('disabled', false);
    $('.editmode, .viewmode').removeClass('disabled');
    multiselectfix(target);
    sortoutaddons();
    return false;
}

function multiselectfix(target) {
    $('.con-multiselect', target).each(function () {
        if ($(this).is(':disabled')) {
            $(this).multiselect('disable');
        } else {
            $(this).multiselect('enable');
        }
    });
}

/**
 * Basic ajax call that is configured using submitSetting global variable
 *
 * @method modalsubmit
 * @param {object} submitconfig  Required global variable from screen specific config.js. Contains a series of sets of parameters, each with a unique key. Each input is paired to the desired set of parameters by attaching a submit-settings data attribute to it containing the key of the set that is required. See examples below.
 * @version
 * @author Tom Yeldham
 *
 *
 * @example
 * 
 *          <button class="btn btn-primary submit" data-submit-settings="modal1submit" form="modal1form" type="submit" aria-hidden="true">Save changes</button>
 *
 * Assume that an modal's submit button has the above mark up
 *
 * The data-submit-settings attribute value is what is used to pair this particular field to the desired set of parameters in config.js. We can see it has a value of table1submit, which corresponds to
 *
 * @example
 * 
 *          "modal1submit": {
 *              // Set the url to your service and the searchid to that of the type service you require from that service
 *              url: "http://maf.dev.m4.net/searchSuggest-1.0/newSearchSuggestServlet",
 *              type: "POST",
 *              data: $(this).parents('div.modal').find('form:visible :input').not('button').serializeArray(),
 *              error: function(){
 *                  alert('Test alternate alert')
 *              }
 *          },
 *
 * url, data, type are all compulsary. beforesend, complete, success, error and select are all optional if you wish to alter the default behaviour of the plugin which is configured to work with the service I have been testing against, by using a different function.
 * They should be configured to conform as the jQuery ajax() settings of the same name. See the following link for full documentation http://api.jquery.com/jQuery.ajax/.
 *
 * If additional parameters are required it is possible to completely override the template and provide a unique ajax function for that table by giving a submitconfig key a parameter of unique. This should be a full ajax request function and obviously shouldnt include any other parameters.
 */
function modalsubmit(submitconfig) {
    //Generic submit function which will accept parameters from a config file like search. To be extended as required by Dhaka.
    var submitdata = $.isFunction(submitconfig.data) ? submitconfig.data() : submitconfig.data;
    var targeturl = submitconfig.url;
    var submittype = submitconfig.type;
    var beforesend = submitconfig.beforesend;
    var cache = submitconfig.cache;
    var complete = submitconfig.complete;
    var unique = submitconfig.unique;
    var success = submitconfig.success;
    var error = submitconfig.error;
    if (unique !== undefined) {
        unique = getExecutableFunction(unique);
        unique();
    } else {
        cache = cache === undefined ? false : cache;
        success = getExecutableFunction(success, function () {
            $('div.modal:visible').modal('hide');
        });
        error = getExecutableFunction(error);
        beforesend = getExecutableFunction(beforesend);
        complete = getExecutableFunction(complete);

        $.ajax({
            cache: cache,
            complete: complete,
            beforeSend: beforesend,
            type: submittype,
            url: targeturl,
            data: submitdata,
            success: function (response, textStatus, jqXHR) {
                if ($('#modalsubmitlog').length > 0) {
                    $('#modalsubmitlog').remove();
                }
                success(response, textStatus, jqXHR);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if ($('#modalsubmitlog').length > 0) {
                    $('#modalsubmitlog').remove();
                }
                $('div.modal-body:visible').prepend('<div id="modalsubmitlog" class="alert alert-error"> <strong>Uh-oh... something seems to have gone wrong</strong><br>The following error has occured: ' + errorThrown + '</div>');
                alertremoval($('#modalsubmitlog'));
                error(jqXHR, textStatus, errorThrown);
            }
        });
    }
}


/**
 * Basic ajax call that is configured using submitSetting global variable. Has the exact same parameter as the modal submit call, however as it is related to the main page submit the error and success functions have default error message related actions as well.
 *
 * @method mainpagesubmit
 * @param {object} submitconfig  Required global variable from screen specific config.js. Contains a series of sets of parameters, each with a unique key. Each input is paired to the desired set of parameters by attaching a submit-settings data attribute to it containing the key of the set that is required. See examples below.
 * @version
 * @author Tom Yeldham
 *
 *
 * @example
 * 
 *          <a class="submit viewmode updatebutton addbutton" id="headerconfirmbutton" data-submit-settings="mainpagesubmit">Confirm</a>
 *
 * The booking screen header confirm button has the above mark up
 *
 * The data-submit-settings attribute value is what is used to pair this particular field to the desired set of parameters in config.js. We can see it has a value of table1submit, which corresponds to
 *
 * @example
 * 
 *   "mainpagesubmit": {
 *       // Append the service specific chunk of the url for this search. This will not change from deploy to deploy.
 *       url: deployspecificurlsection + "newSearchSuggestServlet",
 *       type: "POST",
 *       data: $('form#mainform').find(':input').not('button').serializeArray(),
 *       error: function() {
 *           console.log('Test mainform submit error');
 *       },
 *       success: function() {
 *           console.log('Test mainform submit success');
 *       }
 *   },
 *
 * url, data, type are all compulsary. beforesend, complete, success, error and select are all optional if you wish to alter the default behaviour of the plugin which is configured to work with the service I have been testing against, by using a different function.
 * They should be configured to conform as the jQuery ajax() settings of the same name. See the following link for full documentation http://api.jquery.com/jQuery.ajax/.
 *
 * If additional parameters are required it is possible to completely override the template and provide a unique ajax function for that table by giving a submitconfig key a parameter of unique. This should be a full ajax request function and obviously shouldnt include any other parameters.
 */

function mainpagesubmit(submitconfig) {
    //Generic submit function which will accept parameters from a config file like search. To be extended as required by Dhaka.
    var submitdata = $.isFunction(submitconfig.data) ? submitconfig.data() : submitconfig.data;
    var targeturl = submitconfig.url;
    var submittype = submitconfig.type;
    var beforesend = submitconfig.beforesend;
    var cache = submitconfig.cache;
    var complete = submitconfig.complete;
    var unique = submitconfig.unique;
    var success = submitconfig.success;
    var error = submitconfig.error;
    if (unique !== undefined) {
        unique = getExecutableFunction(unique);
        unique();
    } else {
        cache = cache === undefined ? false : cache;
        success = getExecutableFunction(success);
        error = getExecutableFunction(error);
        beforesend = getExecutableFunction(beforesend);
        complete = getExecutableFunction(complete);

        $.ajax({
            complete: complete,
            beforeSend: beforesend,
            type: submittype,
            url: targeturl,
            data: submitdata,
            success: function (data, status, xhr) {
                $.postObjectOnSuccess(data, status, xhr, success);
            },
            error: function (xhr, status, errorThrown) {
                $.postObjectOnError(xhr, status, errorThrown, error);
            }
        });
    }
}



/**
 * Basic ajax call that is configured using submitSetting global variables
 * @method inroweditsubmit
 * @param {object} submitconfig  Required global variable from screen specific config.js. Contains a series of sets of parameters, each with a unique key. Each input is paired to the desired set of parameters by attaching a submit-settings data attribute to it containing the key of the set that is required. See examples below.
 * @version
 * @author Tom Yeldham
 *
 * @example
 * 
 *      <button id="newedittableconfirmchangesbutton" class="btn btn-mini-icon pull-right inrowedittableconfirmchangesbutton" data-submit-settings="table1submit"><i class="icon-ok"></i></button>
 *
 * Assume that an inrowedittable submit button has the above mark up.
 *
 * The data-submit-settings attribute value is what is used to pair this particular field to the desired set of parameters in config.js. We can see it has a value of table1submit, which corresponds to
 *
 * @example
 * 
 *      "table1submit": {
 *          // Set the url to your service and the searchid to that of the type service you require from that service
 *          url: "http://maf.dev.m4.net/searchSuggest-1.0/newSearchSuggestServlet",
 *          type: "POST",
 *          data: $(this).parents('table').find('tbody :input').not('button').serializeArray(),
 *          error: function(){
 *              alert('Test alternate alert')
 *          }
 *      },
 *
 *
 * url, data, type are all compulsary. beforesend, complete, success, error and select are all optional if you wish to alter the default behaviour of the plugin which is configured to work with the service I have been testing against, by using a different function.
 * They should be configured to conform as the jQuery ajax() settings of the same name. See the following link for full documentation http://api.jquery.com/jQuery.ajax/.
 *
 * If additional parameters are required it is possible to completely override the template and provide a unique for that table by giving a submit config key a parameter of unique. This should be a full ajax request function and obviously shouldnt include any other parameters.
 */

function inroweditsubmit(submitconfig, that) {
    //Generic submit function which will accept parameters from a config file like search. To be extended as required by Dhaka.
    var submitdata = $.isFunction(submitconfig.data) ? submitconfig.data() : submitconfig.data;
    var targeturl = submitconfig.url;
    var submittype = submitconfig.type;
    var beforesend = submitconfig.beforesend;
    var cache = submitconfig.cache;
    var complete = submitconfig.complete;
    var unique = submitconfig.unique;
    var success = submitconfig.success;
    var error = submitconfig.error;
    var parentform = submitconfig.form;
    if (unique !== undefined) {
        unique = getExecutableFunction(unique);
        unique();
    } else {
        success = getExecutableFunction(success, function (returneddata) {
        });
        error = getExecutableFunction(error, function () {
            alert('Sorry, something went wrong while trying to submit your changes');
        });
        beforesend = getExecutableFunction(beforesend);
        complete = getExecutableFunction(complete);
        cache = cache === undefined ? false : cache;
        submittype = submittype === undefined ? parentform.attr("method") : submittype;
        targeturl = targeturl === undefined ? parentform.attr("action") : targeturl;
        submitdata = submitdata === undefined ? parentform.serializeArray() : submitdata;

        $.ajax({
            cache: cache,
            complete: complete,
            beforeSend: beforesend,
            type: submittype,
            url: targeturl,
            data: submitdata,
            success: function (data, status, xhr) {
                // Binding actions for reloaded events
                if (data.reloads !== undefined) {
                    for (var key in data.reloads) {
                        var targetelement = $('#' + key).parent();
                        targetelement.unbind();
                        targetelement.on('reloaded', function () {
                            setvalidator();
                            metaboot($(this));
                            addvalidation($(this));
                            viewtable($(this));
                            inrowEditTableInitialisation($(this));
                            success(data);
                        });
                    }
                    // trigger reloaded 
                    $.postObjectOnSuccess(data, status, xhr);
                } else {
                    $.postObjectOnSuccess(data, status, xhr, success);
                    viewtable(that);
                }
            },
            error: function (xhr, status, errorThrown) {
                $.postObjectOnError(xhr, status, errorThrown, error);
            }
        });
    }
}



/**
 * Initialises keyboard nav for grid tables
 * @method setuptablenav
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @return false
 * @version
 * @author Tom Yeldham
 */
function setuptablenav(target) {
    $('td :input:not("button")', target).on('keydown', function (event) {
        var rowlength = $(this).parents('tr').children('td').length;
        var rowcount = $(this).parents('tbody').find('tr').length;
        var column = $(this).parents('tr').children('td').index($(this).parents('td'));
        var row = $(this).parents('tbody').find('tr').index($(this).parents('tr'));
        var parsedcolumn = parseInt(column);
        var parsedrow = parseInt(row);
        parsedcolumn++;
        parsedrow++;
        var lastinputcell = parseInt(rowlength) - 1;
        //ENTER
        if (event.shiftKey && event.which === 13) {
            event.preventDefault();
            $(this).parents('table').find('button.gridtableconfirmchangesbutton').trigger('click');
        }
        //DELETE
        if (event.shiftKey && event.which === 46) {
            event.preventDefault();
            if ($(this).parents('tr').find('button.deleterow:visible').length > 0) {
                var targetcell;
                if (parsedrow !== rowcount) {
                    targetcell = $(this).parents('tr').nextAll("tr:visible").first().find("td:nth-child(" + parsedcolumn + ") :input");
                } else {
                    targetcell = $(this).parents('tr').prevAll("tr:visible").first().find("td:nth-child(" + parsedcolumn + ") :input");
                }
                $(this).parents('tr').find('button.deleterow:visible').trigger('click');
                targetcell.focus();
            }
        }
        //BACKSPACE
        if (event.shiftKey && event.which === 8) {
            event.preventDefault();
            $(this).parents('table').find('button.gridtablecancelchangesbutton').trigger('click');
        }
        //DOWN
        if (event.shiftKey && event.which === 40) {
            event.preventDefault();
            if (parsedrow !== rowcount) {
                $(this).parents('tr').nextAll("tr:visible").first().find("td:nth-child(" + parsedcolumn + ") :input").focus();
            } else {
                $(this).parents('table').find('button.gridtablenewbutton').trigger('click');
                $(this).parents('tr').nextAll("tr:visible").first().find("td:nth-child(" + parsedcolumn + ") :input").focus();
            }
            return false;
        }
        //LEFT
        if (event.shiftKey && event.which === 37) {
            event.preventDefault();
            if (parsedcolumn === 1 && row > 0) {
                $(this).parents('tr').prevAll("tr:visible").first().find("td:nth-child(" + lastinputcell + ") :input").focus();
            } else {
                $(this).parent('td').prev().children("input").focus();
            }
            return false;
        }
        //RIGHT
        if (event.shiftKey && event.which === 39) {
            event.preventDefault();
            if (lastinputcell === parsedcolumn && parsedrow !== rowcount) {
                $(this).parents('tr').nextAll("tr:visible").first().find("td:first:has(':input') :input").focus();
            } else {
                $(this).parent('td').next().children("input").focus();
            }
            return false;
        }
        //UP
        if (event.shiftKey && event.which === 38) {
            event.preventDefault();
            if (row > 0) {
                $(this).parents('tr').prevAll("tr:visible").first().find("td:nth-child(" + parsedcolumn + ") :input").focus();
            }
            return false;
        }
    });
}


function sortoutaddons(target) {
    $('span.add-on', target).each(function () {
        if ($(this).prevAll(':input:first').is(":disabled")) {
            $(this).addClass("disabledaddon");
        } else {
            $(this).removeClass("disabledaddon");
        }
    });
}

/**
 * Adjusts the scroll for the subnavbar. Called on any function that causes a change in vertical layout
 * @method adjustscroll
 * @version
 * @author Tom Yeldham
 */

// Adjusts the scroll for the subnavbar. Called on any function that causes a change in vertical layout
function adjustscroll(target) {
    if ($('#subnavscroll.subnav').length > 0) {
        $('body').scrollspy('refresh');
    }
    initstyledselects(target);
}

/**
 * Generic function to make sure that when edittable table rows are added or deleted, the cells have their id's and names updated to ensure that there are no duplicates.
 * @method reidrowinputs
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @version
 * @author Tom Yeldham
 */

function reidrowinputs(target) {
    var tbody = target.find('table.gridtable tbody');
    var tablerows = tbody.find('tr');
    var no = 1;
    $.each(tablerows, function () {
        var rowinputs = $(this).find(':input');
        var copiedrowid = $(this).attr('id');
        var correctrownoid = copiedrowid.replace(/[0-9]/g, '' + no + '');
        $(this).attr('id', correctrownoid);
        var newrownewid = $(this).attr('id');
        $.each(rowinputs, function () {
            var thisid = $(this).attr('id');
            if (thisid) {
                var elementid = thisid.replace(copiedrowid, '');
                $(this).attr('id', '' + newrownewid + elementid + '');
            }
            var thisname = $(this).attr('name');
            if (thisname) {
                var elementname = thisname.replace(copiedrowid, '');
                $(this).attr('name', '' + newrownewid + elementname + '');
            }
        });
        no++;
    });
}

var findIndexOfKey = function (searchKey) {
    for (var i = 0; i < localStorage.length; i++) {
        var key = localStorage.key(i);
        if (key === searchKey)
            return i;
    }
    return -1;
};

/**
 * Checks the function reference existence and returns executable function reference. Executable function reference can be
 * default function reference if reference is undefined and valid default function reference provided. 
 * @method getExecutableFunction
 * @param reference Function reference
 * @param defaultFunction Default functiion to use as function reference if provided function reference is undefined
 * @author Noor
 * @returns
 */
// TODO Need to check whether jquery already have the functionality
function getExecutableFunction(reference, defaultFunction) {
    if ($.isFunction(reference) === false) {
        if ($.isFunction(window[reference]) === true) {
            reference = window[reference];
        } else {
            if (defaultFunction === undefined || $.isFunction(defaultFunction) === false) {
                defaultFunction = function () {
                };
            }
            reference = defaultFunction;
        }
    }
    return reference;
}



/**
 * Custom jQuery api to filter the attributes starts with the prefix provided and prepares and returns an array where key is rest of the attribute name and value is attribute value 
 * 
 * @param prefix Attribute name prefix which filters the attributes starts with the prefix 
 * 
 * @example <input type="text" data-search-url="abcd.html" data-search-dependentfields="destination,availdestination" id="searchtest"/>
 *          Now to use the function we can just call as
 *                    var data = $("#searchtest").attributesAsArray("data-search-");
 *                    
 *          Here is the array [{url:"abcd.html",dependentsfields:"destination,availdestination"}]
 * 
 * @author noor
 */
(function ($) {
    $.fn.attributesAsArray = function (prefix) {
        var attributes = {};
        prefix = prefix === undefined || prefix === "" ? "" : (prefix.lastIndexOf("-") === (prefix.length - 1) ? prefix : prefix + "-");
        if (this.length) {
            $.each(this[0].attributes, function (index, attr) {
                if (attr.name.substring(0, prefix.length) == prefix) {
                    attributes[attr.name.substring(prefix.length)] = attr.value;
                }
            });
        }

        return attributes;
    };
})(jQuery);

function get_browser() {
    var ua = navigator.userAgent, tem, M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if (/trident/i.test(M[1])) {
        tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
        return 'IE ' + (tem[1] || '');
    }
    if (M[1] === 'Chrome') {
        tem = ua.match(/\bOPR\/(\d+)/);
        if (tem !== null) {
            return 'Opera ' + tem[1];
        }
    }
    M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
//    if ((tem == ua.match(/version\/(\d+)/i)) !== null) {
//        M.splice(1, 1, tem[1]);
//    }
    return M[0];
}

function get_browser_version() {
    var ua = navigator.userAgent, tem, M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if (/trident/i.test(M[1])) {
        tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
        return 'IE ' + (tem[1] || '');
    }
    if (M[1] === 'Chrome') {
        tem = ua.match(/\bOPR\/(\d+)/);
        if (tem !== null) {
            return 'Opera ' + tem[1];
        }
    }
    M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
//    if ((tem == ua.match(/version\/(\d+)/i)) !== null) {
//        M.splice(1, 1, tem[1]);
//    }
    return M[1];
}

function headeralertfix() {
    $('div.alert.headeralert').css("width", $(".container").width() + "px")
            .css("top", ($("body > div > div.navbar, body > div.navbar").height() + $("div.subnav").height()) + "px")
            .css("left", ($('body').width() - $(".container").width()) / 2 + "px");
}

function initstyledselects(target) {
    $('select:visible', target).not('.con-multiselect, [multiple]').each(function () {
        if ($(this).parent('.styled-select').length === 0) {
            var width = $(this).outerWidth();
            var frameworkfix = "";
            var nodisplay = "";
            var disabled = "";
            if (get_browser() === "Firefox" && get_browser_version() === "14") {
                frameworkfix = "frameworkfix";
            }
            if (get_browser() === "Chrome") {
                frameworkfix = "chromeselectfix";
            }
            if ($(this).is('.nodisplay')) {
                nodisplay = "nodisplay";
            }
            if ($(this).is(':disabled')) {
                disabled = "disabled";
            }
            //Fixes selects being too short and difficult!
            width = width + 14;
            if ($(this).is('.input-singlecharacter')) {
                width = width + 10;
            }
            $(this).wrap('<div class="styled-select ' + nodisplay + ' ' + disabled + ' ' + frameworkfix + '" style="width:' + width + 'px"></div>');
            $(this).after('<i class="icon icon-chevron-down"></i>');
            $(this).css('width', width + 18 + 'px');
        }
    });
}

function generateGantt(ganttchart, driverarray) {
//    function convertTime(time) {
//        var a = time.split(':');
//        return (((+a[0]) * 60 * 60 + (+a[1]) * 60) / 86400) * 100;
//    }
//
//    ganttchart.find('.ganttlist').empty();
//    driverarray.forEach(function (driver) {
//        var runblock = '';
//        var prevwidth = 0;
//        driver.runs.forEach(function (run) {
//            runblock = runblock + '<span class="gantrun" style="left:' + (convertTime(run.starttime) - prevwidth) + '%; width:' + (convertTime(run.endtime) - convertTime(run.starttime)) + '%">' + run.runname + ' (' + run.starttime + ' - ' + run.endtime + ')</span>';
//            prevwidth = (convertTime(run.endtime) - convertTime(run.starttime));
//        });
//        ganttchart.find('.ganttlist').append('<li class="gantrow"><div class="gantname">' + driver.name + '</div><div class="gantchartrow">' + runblock + '</div></li>');
//    });
//    ganttchart.find('.ganttlist').append('<li class="gantrow ganttimeline">\n\
//    <div class="gantname"></div>\n\
//    <div class="gantchartrow">\n\
//        <span class="gantrun">\n\
//            <span class="hourmarker"><span class="hourno">0:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno">1:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno">2:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno">3:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno">4:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno">5:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno">6:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno">7:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno">8:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno">9:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">10:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">11:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">12:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">13:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">14:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">15:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">16:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">17:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">18:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">19:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">20:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">21:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">22:00</span></span>\n\
//            <span class="hourmarker"><span class="hourno doubledigit">23:00</span></span>\n\
//            <span class="lasthourno">0:00</span>\n\
//            <span class="currenttime"></span>\n\
//        </span>\n\
//    </div>\n\
//</li>');
//    metaboot(ganttchart.find('.ganttlist'));
//
//    function adjustTimeNow(gantt) {
//        gantt.find('.currenttime').css('height', gantt.find('.ganttlist').height() - 10 + 'px').css('top', '-' + (gantt.find('.ganttlist').height() - 15) + 'px').css('left', convertTime(new Date().getHours().toString() + ':' + new Date().getMinutes().toString()) + '%');
//    }
//    adjustTimeNow(ganttchart);
//    setInterval(function () {
//        adjustTimeNow(ganttchart);
//        console.log('Updated Gantt');
//    }, 30000);

}


/* $id$ */
/**
 * @class view&editEventHandlers
 */

/**
 * Initialises .viewmode and .editmode buttons in the header. Takes global variables of editcallbacks and viewcallbacks from screen specific editviewmodecallbacks.js
 * @method viewEditModelEventHandlingInitialisation
 * @param {Jquery object} target Required parameter to localise the area of the DOM that the script selectors will initialise over.
 * @param {object} editcallbacks Required global variable found in the screen specific editviewmodecallbacks.js file. Used to tie ajax calls and additional funcitonality into 3 events (editpre, editduring, editdone) fired upon the .editmode button click in the headerbar.
 * @param {object} viewcallbacks Required global variable found in the screen specific editviewmodecallbacks.js file. Used to tie ajax calls and additional funcitonality into 3 events (viewpre, viewduring, viewdone) fired upon the .viewmode button click in the headerbar.
 * @version
 * @author Tom Yeldham
 *
 * @example 
 *      var editcallbacks = {
 *          editpre:  function (that){
 *              console.log('editpre')
 *          },
 *          editduring: function (that){
 *              console.log('editduring')
 *          },
 *          editdone: function (that){
 *              console.log('editdone')
 *          }
 *      }; 
 *      var viewcallbacks = {
 *          viewpre:  function (that){
 *              console.log('viewpre')
 *          },
 *          viewduring: function (that){
 *              console.log('viewduring')
 *          },
 *          viewdone: function (that){
 *              console.log('viewdone')
 *          }
 *      }; 
 */

// VIEW/MODE EVENT HANDLING

function viewEditModeEventHandlingInitialisation(target) {
    $('button.editmode, a.editmode', target).on("click", editcallbacks, function(event) {
        //genericpreloadstuff();
        if (!$(this).is('.disabled')) {
            if (event.data.editpre) {
                event.data.editpre($(this));
            }
            $(this).trigger('preclickstuffdone');
        }
    });

    $('button.editmode, a.editmode', target).on("preclickstuffdone", editcallbacks, function(event) {
        if (event.data.editduring) {
            event.data.editduring($(this));
        }
        $(this).trigger('duringstuffdone');
    });

    $('button.editmode, a.editmode', target).on("duringstuffdone", editcallbacks, function(event) {
        var param2 = $(this).attr('id');
        editmode(target, param2);
        if (event.data.editdone) {
            event.data.editdone($(this));
        }
    });

    $('button.viewmode, a.viewmode', target).on("click", viewcallbacks, function(event) {
        if (!$(this).is('.disabled')) {
            if ($('.gridtableconfirmchangesbutton:visible').length > 0) {
                alert('Please submit or cancel your table changes first');
            }
            else {
                if ($(this).hasClass('submit')) {
                    var targetform = null;
                    if ($(this).is('#headerconfirmbutton')) {
                        targetform = $('form#mainform');
                    }
                    if ($(this).is('[form]')) {
                        targetform = $(this).attr('form');
                    }
                    if ($(this).data('targetform')) {
                        targetform = $(this).data('targetform');
                    }
                    if (!targetform)
                        targetform = $(this).parents("form");

                    if (targetform.valid()) {
                        if (event.data.viewsubmitpre) {
                            event.data.viewsubmitpre($(this));
                        }
                        $(this).trigger('preclicksubmitstuffdone');
                    }
                }
                else {
                    if (event.data.viewcancelpre) {
                        event.data.viewcancelpre($(this));
                    }
                    $(this).trigger('preclickcancelstuffdone');
                    $('.error').removeClass('error');
                    $('div.help-block').remove();
                    viewmode(target);
                }
            }
        }
    });

    $('button.viewmode, a.viewmode', target).on("preclicksubmitstuffdone", viewcallbacks, function(event) {
        //genericduringstuff();
        var submitsettings = $(this).data('submit-settings');
        var submitconfig = submitSetting[submitsettings];
        if (submitsettings === undefined || submitsettings === "" || submitconfig === undefined) {
            submitconfig = $(this).attributesAsArray("data-submit-");
        }
        // Set mainform data if headerconfirmbutton is clicked
        if ($(this).is('#headerconfirmbutton')) {
            targetform = $('form#mainform');
            if (submitconfig.data === undefined)
                submitconfig.data = $('form#mainform').serialize();
            if (submitconfig.url === undefined)
                submitconfig.url = $('form#mainform')[0].action;
            if (submitconfig.type === undefined)
                submitconfig.type = 'POST';
        }
        var ev = $.Event("mainpagesubmitinitialise"); // Jquery event triggering machanism
        $(this).trigger(ev, submitconfig);
        if (ev.isDefaultPrevented()) return; // mainpagesubmitinitialise event returned false or prevented default, so ignoring next default operations
        mainpagesubmit(submitconfig);
        if (event.data.viewsubmitduring) {
            event.data.viewsubmitduring($(this));
        }
        $(this).trigger('duringsubmitstuffdone');
    });

    $('button.viewmode, a.viewmode', target).on("duringsubmitstuffdone", viewcallbacks, function(event) {
        viewmode(target);
        if (event.data.viewsubmitdone) {
            event.data.viewsubmitdone($(this));
        }
    });

    $('button.viewmode, a.viewmode', target).on("preclickcancelstuffdone", viewcallbacks, function(event) {
        //genericduringstuff();
        if (event.data.viewcancelduring) {
            event.data.viewcancelduring($(this));
        }
        $(this).trigger('duringcancelstuffdone');
    });

    $('button.viewmode, a.viewmode', target).on("duringcancelstuffdone", viewcallbacks, function(event) {
        viewmode(target);
        $('.error').removeClass('error');
        $('div.help-block').remove();
        if (event.data.viewcanceldone) {
            event.data.viewcanceldone($(this));
        }
    });

}



function setvalidator(target) {

    function roundNumber(num, dec) {
        var result = Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
        return result;
    }

    // Setting where/how the validation errors are displayed for different types of controls in different formats
    $("#mainform, div.modal form, .otherbodyform", target).not('div.modal form:has("table.inrowedittable")').each(function () {
        $(this).validate({
            onkeyup: function (element) {
                if (!$(element).is('input.con-datepicker, input.con-startdate, input.con-enddate, input.con-futuredate, input.con-pastdate, input.con-timepicker, input.con-dobfield, input.con-starttime, input.con-endtime')) {
                    if (!this.checkable(element) && (element.name in this.submitted || !this.optional(element))) {
                        this.element(element);
                    }
                }
            },
            onfocusout: function (element) {
                if (!$(element).is('input.con-datepicker, input.con-startdate, input.con-enddate, input.con-futuredate, input.con-pastdate, input.con-timepicker, input.con-dobfield, input.con-starttime, input.con-endtime')) {
                    if (!this.checkable(element) && (element.name in this.submitted || !this.optional(element))) {
                        this.element(element);
                    }
                }
            },
            errorElement: "span",
            wrapper: "div", // a wrapper around the error message

            //Error message mark up
            errorPlacement: function (error, element) {
                if ($(element).data('toggledby')) {
                    element.after(error);
                    error.addClass('help-block error');  // add a class to the wrapper
                    error.css('margin', '0');
                } else if ($(element).siblings().hasClass('radio')) {
                    element.after(error);
                    error.addClass('help-block error');  // add a class to the wrapper
                    error.css('margin', '0');
                } else if ($(element).is('[data-datetimepair]')) {
                    element.parents('div.controls').find('span.error').remove();
                    element.parents('div.controls').append(error);
                    error.addClass('help-block error');  // add a class to the wrapper
                    error.css('margin', '0');
                } else if ($(element).is('.con-startdate, .con-enddate')) {
                    element.parents('div.controls').find('span.error').remove();
                    element.parents('div.controls').append(error);
                    error.addClass('help-block error');  // add a class to the wrapper
                    error.css('margin', '0');
                } else if ($(element).parent().hasClass('input-append')) {
                    element.parent().append(error);
                    error.addClass('help-block error');  // add a class to the wrapper
                    error.css('margin', '0');

                } else {
                    element.parents('div.controls').append(error);
                    error.addClass('help-block');  // add a class to the wrapper
                    error.css('margin', '0');
                }
            },
            //Where error class is applied to the element
            highlight: function (element, error) {
                var datepair = "";
                if ($(element).data('toggledby')) {
                    $(element).addClass("error");
                } else if ($(element).siblings().hasClass('radio')) {
                    $(element).addClass("error");
                } else if ($(element).is('[data-datetimepair]')) {
                    datepair = $(element).data("datetimepair");
                    $("input[data-datetimepair=" + datepair + "]").parent().addClass("error");
                } else if ($(element).is('.con-startdate, .con-enddate')) {
                    datepair = $(element).data("datepair");
                    $("input[data-datepair=" + datepair + "]").parent().addClass("error");
                } else if ($(element).parent().hasClass('input-append')) {
                    $(element).parent().addClass("error");
                } else {
                    $(element).addClass("error");
                    $(element).parents("div.controls").addClass("error");
                }
            },
            unhighlight: function (element, error) {
                var datepair = "";
                if ($(element).data('toggledby')) {
                    $(element).removeClass("error");
                } else if ($(element).siblings().hasClass('radio')) {
                    $(element).removeClass("error");
                } else if ($(element).is('[data-datetimepair]')) {
                    datepair = $(element).data("datetimepair");
                    $("input[data-datetimepair=" + datepair + "]").parent().removeClass("error");
                } else if ($(element).is('.con-startdate, .con-enddate')) {
                    datepair = $(element).data("datepair");
                    $("input[data-datepair=" + datepair + "]").parent().removeClass("error");
                } else if ($(element).parent().hasClass('input-append')) {
                    $(element).parent().removeClass("error");
                } else {
                    $(element).removeClass("error");
                    $(element).parents("div.controls")
                            .removeClass("error");
                }
            },
            focusInvalid: false,
            ignore: ':hidden:not("div.control-group:visible .con-multiselect")'
        });
    });

    //Different for table cells because of different mark up

    $("form:has(table)", target).not('#mainform').each(function () {
        $(this).validate({
            onkeyup: function (element, event) {
                if (event.which === 16 && this.elementValue(element) === '') {
                    return;
                } else {
                    if (!$(element).is('input.con-datepicker, input.con-startdate, input.con-enddate, input.con-futuredate, input.con-pastdate, input.con-timepicker, input.con-dobfield, input.con-starttime, input.con-endtime')) {
                        if (!this.checkable(element) && (element.name in this.submitted || !this.optional(element))) {
                            this.element(element);
                        }
                    }
                }
            },
            errorElement: "span",
            wrapper: "div", // a wrapper around the error message
            errorPlacement: function (error, element) {
                var width = $(element).width();
                var elementid = element.attr('id');
                var elementparentcell = $('#' + elementid).parents('td');
                error.appendTo(elementparentcell);
                error.addClass('help-block');  // add a class to the wrapper
                error.css('margin', '0');
                error.css('width', width);
            },
            showErrors: function (errorMap, errorList) {
                for (var i = 0; errorList[i]; i++) {
                    var element = this.errorList[i].element;
                    //solves the problem with brute force
                    //remove existing error label and thus force plugin to recreate it
                    //recreation == call to errorplacement function
                    this.errorsFor(element).remove();
                }
                this.defaultShowErrors();
            },
            highlight: function (element, error) {
                $(element).addClass("error");
                $(element).parents("td").addClass("error");
            },
            unhighlight: function (element, error) {
                $(element).removeClass("error");
                $(element).parents("td").removeClass("error");
            },
            focusInvalid: false
        });
    });

    //Setting the default error messages for the defaultvalidation rules

    jQuery.extend(jQuery.validator.messages, {
        required: "This field is required.",
        remote: "Code already exists",
        email: "Please enter a valid email address.",
        url: "Please enter a valid URL.",
        date: "Please enter a valid date.",
        dateISO: "Please enter a valid date (ISO).",
        number: "Please enter a valid number.",
        digits: "Whole numbers only",
        creditcard: "Please enter a valid credit card number.",
        equalTo: "Please enter the same value again.",
        accept: "Please enter a value with a valid extension.",
        maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
        minlength: jQuery.validator.format("Please enter at least {0} characters."),
        rangelength: jQuery.validator.format("Please enter a value between {0} and {1} characters long."),
        range: jQuery.validator.format("Please enter a value between {0} and {1}."),
        max: jQuery.validator.format("Please enter a value less than or equal to {0}."),
        min: jQuery.validator.format("Please enter a value greater than or equal to {0}.")
    });

    //    jQuery.validator.addMethod("require_from_group", function(value, element, options) {
    //        var numberRequired = options[0];
    //        var selector = options[1];
    //        var fields = $(selector);
    //        var filled_fields = fields.filter(function() {
    //            // it's more clear to compare with empty string
    //            return $(this).val() != ""; 
    //        });
    //        // we will mark only first empty field as invalid
    //        if (filled_fields.length < numberRequired) {
    //            fields.focus();
    //            return false;
    //        }
    //        else {       
    //            fields.focus();
    //            return true;
    //        }
    //    // {0} below is the 0th item in the options field
    //    }, jQuery.format("Please fill out at least {0} of these fields."));

    //    jQuery.validator.addMethod("require_from_group", function(value, element, options) {
    //        var elems = $(element).parents('form').find(selector);
    //        var numberRequired = options[0];
    //        var selector = options[1];
    //        //Look for our selector within the parent form
    //        var validOrNot = $(selector, element.form).filter(function() {
    //            // Each field is kept if it has a value
    //            return $(this).val();
    //        // Set to true if there are enough, else to false
    //        }).length >= numberRequired;
    //        if(!$(element).data('being_validated')) {
    //            var fields = $(selector, element.form);
    //            var validator = this;
    //            fields.each(function(){
    //                validator.valid(this);
    //            });
    //        }
    //        return validOrNot;
    //    // {0} below is the 0th item in the options field
    //    }, jQuery.format("Please fill out at least {0} of these fields."));
    //
    //
    //    $(":input[data-required='grouprequired']").each(function() {    
    //        var reqno = $(this).data("numberrequired");
    //        var group = $(this).data("requiredgroup");
    //        $(this).rules('add', {
    //            require_from_group: [reqno,"[data-requiredgroup="+group+"]"]
    //        });
    //    });  


    //Adding custom rules to the validator

    jQuery.validator.addMethod("positivenumber", function (value, element) {
        return this.optional(element) || /^[+]?([.]\d+|\d+[.]?\d*)$/.test(value);
    }, "Can't be a negative");

    //Overriding default implementation for 14+digit bug, where it treats value as string
    jQuery.validator.addMethod("max", function (value, element, param) {
        return this.optional(element) || (value * 1.0) <= (param * 1.0);
    }, '');

    jQuery.validator.addMethod("decimalplaces", function (value, element, param) {
        regex = new RegExp("^[0-9]*(\.[0-9]{1," + param + "})?$");
        return this.optional(element) || regex.test(value);
    }, "This has been set up wrong, sorry");

    jQuery.validator.addMethod("cvc", function (value, element) {
        return this.optional(element) || /^\d{3,4}$/.test(value);
    }, "Please enter 3 digit cvc code");

    jQuery.validator.addMethod("price", function (value, element) {
        return this.optional(element) || /^[0-9]*(\.[0-9]{1,2})?$/.test(value);
    }, "Prices can only have 2 decimal places");

    jQuery.validator.addMethod("threedecimalplaceprice", function (value, element) {
        return this.optional(element) || /^[0-9]*(\.[0-9]{1,3})?$/.test(value);
    }, "Prices can only have 3 decimal places");

    jQuery.validator.addMethod("alpha", function (value, element) {
        return this.optional(element) || /^\s*[a-zA-Z,\s]+\s*$/.test(value);
    }, "Letters only please");

    jQuery.validator.addMethod("code", function (value, element) {
        return this.optional(element) || /^[A-Za-z0-9]+$/.test(value);
    }, "Alphanumeric only");

    jQuery.validator.addMethod("postcode", function (value, element) {
        return this.optional(element) || /^([A-PR-UWYZ]([0-9]{1,2}|([A-HK-Y][0-9]|[A-HK-Y][0-9]([0-9]|[ABEHMNPRV-Y]))|[0-9][A-HJKS-UW])\ [0-9][ABD-HJLNP-UW-Z]{2}|(GIR\ 0AA)|(SAN\ TA1)|(BFPO\ (C\/O\ )?[0-9]{1,4})|((ASCN|BBND|[BFS]IQQ|PCRN|STHL|TDCU|TKCA)\ 1ZZ))$/.test(value);
    }, "Sorry we don't know where that is");

    jQuery.validator.addMethod("zipcode", function (value, element) {
        return this.optional(element) || /^[0-9]{5}(-[0-9]{4})?$/.test(value);
    }, "Sorry we don't know where that is");


    jQuery.validator.addMethod("ukmobile", function (value, element) {
        return this.optional(element) || /^((07|00447|\+447)\d{9}|(08|003538|\+3538)\d{8,9})$/.test(value);
    }, "Please enter a valid UK mobile number");

    jQuery.validator.addMethod("creditcard", function (value, element) {
        return this.optional(element) || /^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\d{3})\d{11})$/.test(value);
    }, "Please enter a valid credit card number.");

    jQuery.validator.addMethod("phone", function (value, element) {
        return this.optional(element) || /^[0-9+\(\)#\s\/ext-]+$/.test(value);
    }, "Sorry only digits, parenthesis, dashes, plus, spaces or the letters e, x, t are allowed");

    jQuery.validator.addMethod("help", function (value, element) {
        return this.optional(element) || /[^?]$/.test(value);
    }, "Generic help message");

    jQuery.validator.addMethod("regex", function (value, element, param) {
        regex = new RegExp(param);
        return this.optional(element) || regex.test(value);
    }, "This has been set up wrong, sorry");


    jQuery.validator.addMethod("percentage", function (value, element) {
        return this.optional(element) || /^0*(100(\.00?)?|[0-9]?[0-9](\.[0-9][0-9]?)?)%?$/.test(value);
    }, "Between 0 and 100, with up to 2 decimal places");


    $.validator.addMethod("enddate", function (value, element) {
        var datepair = $(element).data("datepair");
        if ($("input[data-datepair=" + datepair + "]").not(".con-enddate").val() !== "") {
            var otherdate = $("input[data-datepair=" + datepair + "]").not(".con-enddate");
            var otherdateid = otherdate.attr('id');
            $("span.error[for='" + otherdateid + "']").hide();
            var startdatevalue = otherdate.val();
            return moment(startdatevalue, "DD-MMM-YYYY") <= moment(value, "DD-MMM-YYYY");
        }
        return true;
    }, "From date can't occur after to date");

    $.validator.addMethod("startdate", function (value, element) {
        var datepair = $(element).data("datepair");
        if ($("input[data-datepair=" + datepair + "]").not(".con-startdate").val() !== "") {
            var otherdate = $("input[data-datepair=" + datepair + "]").not(".con-startdate");
            var otherdateid = otherdate.attr('id');
            $("span.error[for='" + otherdateid + "']").hide();
            var enddatevalue = otherdate.val();
            return moment(enddatevalue, "DD-MMM-YYYY") >= moment(value, "DD-MMM-YYYY");
        }
        return true;
    }, "From date can't occur before to date");

    $.validator.addMethod("datetimecheck", function (value, element) {
        var pairing = $(element).data('datetimepair');
        var fromdatefield = $('.con-startdate[data-datetimepair="' + pairing + '"]');
        var todatefield = $('.con-enddate[data-datetimepair="' + pairing + '"]');
        var fromtimefield = $('.con-starttime[data-datetimepair="' + pairing + '"]');
        var totimefield = $('.con-endtime[data-datetimepair="' + pairing + '"]');
        var fromdate = fromdatefield.val();
        var todate = todatefield.val();
        var fromtime = fromtimefield.val().replace(/[\.,-\/#!$ï¿½%\^& \*;:{}=\-_`~()a-zA-Z]/g, '');
        var totime = totimefield.val().replace(/[\.,-\/#!$ï¿½%\^& \*;:{}=\-_`~()a-zA-Z]/g, '');
        var fromdatefieldid = $('.con-startdate[data-datetimepair="' + pairing + '"]').attr('id');
        var todatefieldid = $('.con-enddate[data-datetimepair="' + pairing + '"]').attr('id');
        var fromtimefieldid = $('.con-starttime[data-datetimepair="' + pairing + '"]').attr('id');
        var totimefieldid = $('.con-endtime[data-datetimepair="' + pairing + '"]').attr('id');
        $("span.error[for='" + fromdatefieldid + "'], span.error[for='" + todatefieldid + "'], span.error[for='" + fromtimefieldid + "'], span.error[for='" + totimefieldid + "']").hide();
        return !(fromdate !== "" && todate !== "" && fromtime !== "" && totime !== "" && fromdate === todate && fromtime > totime);
    }, "From time can't occur after to time");

    $.validator.addMethod("formatdate", function (value, element) {
        return this.optional(element) || m4dateformat.regex.test(value);
    }, "Please enter a valid date format or use the datepicker by clicking the calendar icon");

    $.validator.addMethod("customformatdate", function (value, element) {
        var customstring = $(element).data("custom-date-format-regex");
        var customregex = RegExp(customstring);
        return this.optional(element) || customregex.test(value);
    }, "Custom format error message");

    $.validator.addMethod("lastdate", function (value) {
        return moment("01-01-2050", "DD-MMM-YYYY") > moment(value, "DD-MMM-YYYY");
    }, "Can't be after 2050");

    $.validator.addMethod("earliestdate", function (value) {
        if (!value)
            return true;
        return moment("01-01-1800", "DD-MMM-YYYY") < moment(value, "DD-MMM-YYYY");
    }, "Can't be before 1800");

    $.validator.addMethod("aftertoday", function (value) {
        var rightnow = moment().startOf('day');
        return moment(rightnow, "DD-MMM-YYYY") <= moment(value, "DD-MMM-YYYY");
    }, "Has to be in the future");

    $.validator.addMethod("beforetoday", function (value) {
        var rightnow = moment().startOf('day');
        return moment(rightnow, "DD-MMM-YYYY") >= moment(value, "DD-MMM-YYYY");
    }, "Has to be in the past");

    $.validator.addMethod("time", function (value, element) {
        return this.optional(element) || /^([01]?[0-9]|2[0-3]):?[0-5][0-9]$/i.test(value);
    }, "Please enter a valid time (1:20, 01:20)");

    $.validator.addMethod("dob", function (value, element) {
        return this.optional(element) || m4dateformat.dobregex.test(value);
    }, m4dateformat.dobregexmessage);

    $.validator.addMethod("email", function (value, element) {
        if (value === "")
            return true;
        var emailregexp = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
        var emails = value.split(/[;,]+/);
        for (var i = 0; i < emails.length; i++) {
            if (!emailregexp.test(emails[i].trim())) {
                return false;
            }
        }
        return true;
    }, "Please use valid email addresses, separated with semi-colons or commas if entering multiple emails");

}

var entityMap = {
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': '&quot;',
    "'": '&#39;',
    "/": '&#x2F;'
};
/**
 * escapes html strings
 * @param string
 * @returns
 */
function escapeHtml(string) {
    return String(string).replace(/[&<>"'\/]/g, function (s) {
        return entityMap[s];
    });
}

//Function to add validation rules to elements within the target

function addvalidation(target) {
    function roundNumber(num, dec) {
        var result = Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
        return result;
    }

    //Function to add validation rules to elements within the target


    //Explanation
    $(":input[data-valid='integer']", target).each(function () {
        $(this).rules("remove", "digits maxlength max min");
        //variables set to check for any validation overrides (data-attributes) on the element
        var maxlength = $(this).data("maxlength");
        var max = $(this).data("max");
        var min = $(this).data("min");
        var maxlengthmessage = $(this).data("maxlengthmessage");
        var maxmessage = $(this).data("maxmessage");
        var minmessage = $(this).data("minmessage");
        //Each option applies the validation rules defined for that option to the element, here digits, maxlength and max
        $(this).rules("add", {
            digits: true,
            maxlength: 6,
            max: 999999
        });
        //Series of checks to see if any of the variables exist and if so to apply the validation overrides to the element
        if (maxlength !== undefined) {
            $(this).rules("add", {
                maxlength: maxlength
            });
        }
        if (max !== undefined) {
            $(this).rules("add", {
                max: max
            });
        }
        if (min !== undefined) {
            $(this).rules("add", {
                min: min
            });
        }
        if (maxlengthmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    maxlength: maxlengthmessage
                }
            });
        }
        if (maxmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    max: maxmessage
                }
            });
        }
        if (minmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    min: minmessage
                }
            });
        }
    });


    $(":input[data-valid='requiredcheckboxes']", target).each(function () {
        $(this).rules("remove", "required minlength");
        var reqno = $(this).data("numberrequired");
        $(this).rules('add', {
            required: true,
            minlength: 1,
            messages: {
                minlength: "Please select 1 or more options"
            }
        });
        if (reqno !== undefined) {
            $(this).rules("add", {
                minlength: reqno,
                messages: {
                    minlength: "Please select " + reqno + " or more options"
                }
            });
        }
    });

    $(":input[data-valid='cvc']", target).each(function () {
        $(this).rules("remove", "cvc");
        $(this).rules('add', {
            cvc: true
        });
    });

    $(":input[data-valid='time']", target).each(function () {
        $(this).rules("remove", "time");
        $(this).rules('add', {
            time: true
        });
    });

    $(":input[data-valid='creditcard']", target).each(function () {
        $(this).rules("remove");
        $(this).rules('add', {
            creditcard: true
        });
    });

    $(":input.con-datepicker:not([data-date-format])", target).each(function () {
        $(this).rules("remove", "formatdate");
        $(this).rules('add', {
            formatdate: true
        });
    });

    $(":input.con-datepicker[data-date-format]", target).each(function () {
        $(this).rules("remove", "customformatdate");
        $(this).rules('add', {
            customformatdate: true
        });
    });

    $(":input.con-enddate", target).each(function () {
        $(this).rules("remove", "formatdate enddate lastdate earliestdate");
        var futuredate = $(this).data("futuredate");
        $(this).rules('add', {
            formatdate: true,
            enddate: true,
            lastdate: true,
            earliestdate: true
        });
        if (futuredate === "Y") {
            $(this).rules("add", {
                aftertoday: true
            });
        }
    });

    $(":input.con-startdate", target).each(function () {
        $(this).rules("remove", "formatdate startdate lastdate earliestdate");
        var futuredate = $(this).data("futuredate");
        $(this).rules('add', {
            formatdate: true,
            startdate: true,
            earliestdate: true,
            lastdate: true
        });
        if (futuredate === "Y") {
            $(this).rules("add", {
                aftertoday: true
            });
        }
    });

    $(":input.con-futuredate", target).each(function () {
        $(this).rules("remove", "formatdate lastdate aftertoday");
        $(this).rules('add', {
            formatdate: true,
            lastdate: true,
            aftertoday: true
        });
    });

    $(":input.con-pastdate", target).each(function () {
        $(this).rules("remove", "formatdate earliestdate beforetoday");
        $(this).rules('add', {
            formatdate: true,
            earliestdate: true,
            beforetoday: true
        });
    });


    $(":input[data-valid='customdecimalplaces']", target).each(function () {
        $(this).rules("remove", "decimalplaces maxlength max min");
        var maxlength = $(this).data("maxlength");
        var max = $(this).data("max");
        var min = $(this).data("min");
        var maxlengthmessage = $(this).data("maxlengthmessage");
        var maxmessage = $(this).data("maxmessage");
        var minmessage = $(this).data("minmessage");
        var decimalplaces = $(this).data("decimalplaces");
        $(this).rules("add", {
            decimalplaces: 2,
            maxlength: 9,
            max: 999999999,
            messages: {
                decimalplaces: "Only accepts " + decimalplaces + " decimal places"
            }
        });
        if (decimalplaces !== undefined) {
            $(this).rules("add", {
                decimalplaces: decimalplaces
            });
        }

        if (maxlength !== undefined) {
            $(this).rules("add", {
                maxlength: maxlength
            });
        }
        if (max !== undefined) {
            $(this).rules("add", {
                max: max
            });
        }
        if (min !== undefined) {
            $(this).rules("add", {
                min: min
            });
        }
        if (maxlengthmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    maxlength: maxlengthmessage
                }
            });
        }
        if (maxmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    max: maxmessage
                }
            });
        }
        if (minmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    min: minmessage
                }
            });
        }
    });

    $(":input[data-valid='price']", target).each(function () {
        $(this).rules("remove", "price maxlength max min");
        var maxlength = $(this).data("maxlength");
        var max = $(this).data("max");
        var min = $(this).data("min");
        var maxlengthmessage = $(this).data("maxlengthmessage");
        var maxmessage = $(this).data("maxmessage");
        var minmessage = $(this).data("minmessage");
        $(this).rules("add", {
            price: true,
            maxlength: 9,
            max: 999999999
        });

        if (maxlength !== undefined) {
            $(this).rules("add", {
                maxlength: maxlength
            });
        }
        if (max !== undefined) {
            $(this).rules("add", {
                max: max
            });
        }
        if (min !== undefined) {
            $(this).rules("add", {
                min: min
            });
        }
        if (maxlengthmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    maxlength: maxlengthmessage
                }
            });
        }
        if (maxmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    max: maxmessage
                }
            });
        }
        if (minmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    min: minmessage
                }
            });
        }
    });

    $(":input[data-valid='threedecimalplaceprice']", target).each(function () {
        $(this).rules("remove", "threedecimalplaceprice maxlength max min");
        var maxlength = $(this).data("maxlength");
        var max = $(this).data("max");
        var min = $(this).data("min");
        var maxlengthmessage = $(this).data("maxlengthmessage");
        var maxmessage = $(this).data("maxmessage");
        var minmessage = $(this).data("minmessage");
        $(this).rules("add", {
            threedecimalplaceprice: true,
            maxlength: 9,
            max: 999999999
        });

        if (maxlength !== undefined) {
            $(this).rules("add", {
                maxlength: maxlength
            });
        }
        if (max !== undefined) {
            $(this).rules("add", {
                max: max
            });
        }
        if (min !== undefined) {
            $(this).rules("add", {
                min: min
            });
        }
        if (maxlengthmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    maxlength: maxlengthmessage
                }
            });
        }
        if (maxmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    max: maxmessage
                }
            });
        }
        if (minmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    min: minmessage
                }
            });
        }
    });

    $(":input[data-valid='number']", target).each(function () {
        $(this).rules("remove", "number maxlength max min");
        var maxlength = $(this).data("maxlength");
        var max = $(this).data("max");
        var min = $(this).data("min");
        var maxlengthmessage = $(this).data("maxlengthmessage");
        var maxmessage = $(this).data("maxmessage");
        var minmessage = $(this).data("minmessage");
        $(this).rules("add", {
            number: true,
            maxlength: 9,
            max: 999999999
        });

        if (maxlength !== undefined) {
            $(this).rules("add", {
                maxlength: maxlength
            });
        }
        if (max !== undefined) {
            $(this).rules("add", {
                max: max
            });
        }
        if (min !== undefined) {
            $(this).rules("add", {
                min: min
            });
        }
        if (maxlengthmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    maxlength: maxlengthmessage
                }
            });
        }
        if (maxmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    max: maxmessage
                }
            });
        }
        if (minmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    min: minmessage
                }
            });
        }
    });

    $(":input[data-valid='percentage']", target).each(function () {
        $(this).rules("remove", "percentage");
        $(this).rules("add", {
            percentage: true
        });
    });

    $(":input[data-valid='cheque']", target).each(function () {
        $(this).rules("remove", "digits maxlength minlength");
        $(this).rules("add", {
            digits: true,
            maxlength: 6,
            minlength: 6,
            messages: {
                minlength: "Cheque numbers need to be 6 digits",
                maxlength: "Cheque numbers need to be 6 digits"
            }
        });
    });

    $(":input[data-valid='age']", target).each(function () {
        $(this).rules("remove", "digits maxlength minlength max min");
        $(this).rules("add", {
            digits: true,
            maxlength: 3,
            minlength: 1,
            max: 120,
            min: 0,
            messages: {
                minlength: "You have to have been born",
                maxlength: "This seems high",
                max: "This seems high",
                min: "You have to have been born"
            }
        });
    });

    $(":input[data-valid='pax']", target).each(function () {
        $(this).rules("remove", "digits maxlength minlength min");
        $(this).rules("add", {
            digits: true,
            maxlength: 2,
            minlength: 1,
            min: 1,
            messages: {
                minlength: "Minimum value of 1",
                maxlength: "Maximum value of 99"
            }
        });
    });

    $(":input[data-valid='infants']", target).each(function () {
        $(this).rules("remove", "digits minlength max");
        $(this).rules("add", {
            digits: true,
            max: 8,
            minlength: 0,
            messages: {
                max: "Maximum value of 8"
            }
        });
    });

    $(":input[data-valid='children']", target).each(function () {
        $(this).rules("remove", "digits maxlength minlength");
        $(this).rules("add", {
            digits: true,
            maxlength: 2,
            minlength: 0,
            messages: {
                maxlength: "Maximum value of 99"
            }
        });
    });

    $(":input[data-valid='adults']", target).each(function () {
        $(this).rules("remove", "digits maxlength minlength min");
        var min = $(this).data("min");
        var minmessage = $(this).data("minmessage");
        $(this).rules("add", {
            digits: true,
            maxlength: 2,
            minlength: 1,
            min: 1,
            messages: {
                maxlength: "Maximum value of 99",
                min: "Minimum value of 1"
            }
        });
        if (min !== undefined) {
            $(this).rules("add", {
                min: min
            });
        }
        if (minmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    min: minmessage
                }
            });
        }
    });

    $(":input[data-valid='distance']", target).each(function () {
        $(this).rules("remove", "help maxlength min positivenumber");
        var converttovalue = $(this).data('convertto');
        var minmessage;
        var helpmessage;
        if (converttovalue === "mi") {
            minmessage = "Positive numbers only. For km -> miles conversion, just type 'km' after the distance and it will be converted for you.";
            helpmessage = "For km -> miles conversion, just type 'km' after the distance and it will be converted for you.";
        } else {
            minmessage = "Positive numbers only. For miles -> km conversion, just type 'mi' after the distance and it will be converted for you.";
            helpmessage = "For miles -> km conversion, just type 'mi' after the distance and it will be converted for you.";
        }
        $(this).rules("add", {
            help: true,
            min: 0,
            maxlength: 8,
            positivenumber: true,
            messages: {
                min: minmessage,
                help: helpmessage
            }
        });
    });

    $(":input[data-valid='distance']", target).keyup(function () {
        var converttovalue = $(this).data('convertto');
        var value;
        var roundednumber;
        var othervalue;
        var roundedmiles;
        if (converttovalue === 'mi') {
            if ($(this).val().match("km")) {
                value = $(this).val().replace(/[^\d\.]/g, '') / 1.609344;
                roundednumber = roundNumber(value, 2);
                $(this).val(roundednumber);
            }
            othervalue = $(this).val().replace(/[^\d\.]/g, '') * 1.609344;
            roundedmiles = roundNumber(othervalue, 2);
            $(this).parents('div.input-append').find('span.add-on').attr("data-original-title", "Or " + roundedmiles + " km. For km -> miles conversion, just type 'km' after the distance and it will be converted for you.");
        } else {
            if ($(this).val().match("mi$")) {
                value = $(this).val().replace(/[^\d\.]/g, '') * 1.609344;
                roundednumber = roundNumber(value, 2);
                $(this).val(roundednumber);
            }
            othervalue = $(this).val().replace(/[^\d\.]/g, '') / 1.609344;
            roundedmiles = roundNumber(othervalue, 2);
            $(this).parents('div.input-append').find('span.add-on').attr("data-original-title", "Or " + roundedmiles + " miles. For miles -> km conversion, just type 'mi' after the distance and it will be converted for you.");
        }
    });

    $(":input[data-valid='weight']", target).each(function () {
        $(this).rules("remove", "help maxlength positivenumber");
        var converttovalue = $(this).data('convertto');
        var positivenumbermessage;
        var helpmessage;
        if (converttovalue === "lb") {
            positivenumbermessage = "Positive numbers only. For kilo -> pound conversion, just type 'kg' after the weight and it will be converted for you.";
            helpmessage = "For kilo -> pound conversion, just type 'kg' after the weight and it will be converted for you.";
        } else {
            positivenumbermessage = "Positive numbers only. For pound -> kilo conversion, just type 'lb' after the weight and it will be converted for you.";
            helpmessage = "For pound -> kilo conversion, just type 'lb' after the weight and it will be converted for you.";
        }
        $(this).rules("add", {
            help: true,
            positivenumber: true,
            maxlength: 8,
            messages: {
                positivenumber: positivenumbermessage,
                help: helpmessage
            }
        });
    });

    $(":input[data-valid='weight']", target).keyup(function () {
        var converttovalue = $(this).data('convertto');
        var value;
        var roundednumber;
        var othervalue;
        var roundedkg;
        if (converttovalue === 'lb') {
            if ($(this).val().match("kg$")) {
                value = $(this).val().replace(/[^\d\.]/g, '') / 0.453592;
                roundednumber = roundNumber(value, 2);
                $(this).val(roundednumber);
            }
            othervalue = $(this).val().replace(/[^\d\.]/g, '') * 0.453592;
            roundedkg = roundNumber(othervalue, 2);
            $(this).parents('div.input-append').find('span.add-on').attr("data-original-title", "Or " + roundedkg + " kg. For kilo -> pound conversion, just type 'kg' after the weight and it will be converted for you.");
        } else {
            if ($(this).val().match("lb$")) {
                value = $(this).val().replace(/[^\d\.]/g, '') * 0.453592;
                roundednumber = roundNumber(value, 2);
                $(this).val(roundednumber);
            }
            othervalue = $(this).val().replace(/[^\d\.]/g, '') / 0.453592;
            roundedkg = roundNumber(othervalue, 2);
            $(this).parents('div.input-append').find('span.add-on').attr("data-original-title", "Or " + roundedkg + " lbs. For pound -> kilo conversion, just type 'lb' after the weight and it will be converted for you.");
        }
    });


    $(":input[data-valid='dims']", target).each(function () {
        $(this).rules("remove", "help maxlength positivenumber");
        var converttovalue = $(this).data('convertto');
        var positivenumbermessage;
        var helpmessage;
        if (converttovalue === "in") {
            positivenumbermessage = "Positive numbers only. For cm -> inch conversion, just type 'cm' after the dimension and it will be converted for you.";
            helpmessage = "For cm -> inch conversion, just type 'cm' after the dimension and it will be converted for you.";
        } else {
            positivenumbermessage = "Positive numbers only. For inch -> cm conversion, just type 'in' after the dimension and it will be converted for you.";
            helpmessage = "For inch -> cm conversion, just type 'in' after the dimension and it will be converted for you.";
        }
        $(this).rules("add", {
            help: true,
            positivenumber: true,
            maxlength: 8,
            messages: {
                positivenumber: positivenumbermessage,
                help: helpmessage
            }
        });
    });

    $(":input[data-valid='dims']", target).keyup(function () {
        var converttovalue = $(this).data('convertto');
        var value;
        var roundednumber;
        var invalue;
        var roundedins;
        if (converttovalue === 'in') {
            if ($(this).val().match("cm$")) {
                value = $(this).val().replace(/[^\d\.]/g, '') / 2.54;
                roundednumber = roundNumber(value, 2);
                $(this).val(roundednumber);
            }
            invalue = $(this).val().replace(/[^\d\.]/g, '') * 2.54;
            roundedins = roundNumber(invalue, 2);
            $(this).parents('div.input-append').find('span.add-on').attr("data-original-title", "Or " + roundedins + "cm. For cm -> inch conversion, just type 'cm' after the dimension and it will be converted for you.");
        } else {
            if ($(this).val().match("in$")) {
                value = $(this).val().replace(/[^\d\.]/g, '') * 2.54;
                roundednumber = roundNumber(value, 2);
                $(this).val(roundednumber);
            }
            invalue = $(this).val().replace(/[^\d\.]/g, '') / 2.54;
            roundedins = roundNumber(invalue, 2);
            $(this).parents('div.input-append').find('span.add-on').attr("data-original-title", "Or " + roundedins + " inches. For inch -> cm conversion, just type 'in' after the dimension and it will be converted for you.");
        }
    });

    $(":input[data-valid='currencytogbp']", target).keyup(function () {
        if ($(this).val().match("usd$")) {
            var value = $(this).val().replace(/[^\d\.]/g, '') * 0.6196;
            var roundednumber = roundNumber(value, 2);
            $(this).val(roundednumber);
        }
        var invalue = $(this).val().replace(/[^\d\.]/g, '') / 0.6196;
        var roundedins = roundNumber(invalue, 2);
        $(this).parents('div.input-append').find('span.add-on').attr("data-original-title", "Or " + roundedins + " USD. For conversion to GBP, just type 'gbp' after the value and it will be converted for you.");
    });

    $(":input[data-valid='currencytogbp']", target).each(function () {
        $(this).rules("remove", "help maxlength positivenumber price");
        $(this).rules("add", {
            help: true,
            positivenumber: true,
            price: true,
            maxlength: 8,
            messages: {
                positivenumber: "Positive numbers only. For conversion to GBP, just type 'gbp' after the value and it will be converted for you.",
                help: "For conversion to GBP, just type 'gbp' after the value and it will be converted for you"
            }
        });
    });

    $(":input[data-valid='text']", target).each(function () {
        $(this).rules("remove", "maxlength");
        var maxlength = $(this).data("maxlength");
        var maxlengthmessage = $(this).data("maxlengthmessage");
        $(this).rules("add", {
            maxlength: 100,
            messages: {
                maxlength: "Value is too long"
            }
        });
        if (maxlength !== undefined) {
            $(this).rules("add", {
                maxlength: maxlength
            });
        }
        if (maxlengthmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    maxlength: maxlengthmessage
                }
            });
        }
    });

    $(":input[data-valid='code']", target).each(function () {
        $(this).rules("remove", "maxlength code minlength url");
        var ob = $(this);
        var maxlength = ob.data("maxlength");
        var minlength = ob.data("minlength");
        var uniquecheckurl = ob.data("unique");
        ob.val(ob.val().toUpperCase());
        ob.rules("add", {
            code: true,
            maxlength: 8,
            minlength: 3,
            messages: {
                minlength: "Value is not long enough",
                maxlength: "Value is too long"
            }
        });
        if (maxlength !== undefined) {
            ob.rules("add", {
                maxlength: maxlength
            });
            var maxlengthmessage = ob.data("maxlengthmessage");
            if (maxlengthmessage !== undefined) {
                ob.rules("add", {
                    messages: {
                        maxlength: maxlengthmessage
                    }
                });
            }
        }
        if (minlength !== undefined) {
            ob.rules("add", {
                minlength: minlength
            });
            var minlengthmessage = ob.data("minlengthmessage");
            if (minlengthmessage !== undefined) {
                ob.rules("add", {
                    messages: {
                        minlength: minlengthmessage
                    }
                });
            }
        }
        if (uniquecheckurl !== undefined) {
            ob.rules("add", {
                remote: {
                    url: uniquecheckurl
                }
            });
            var uniquemessage = ob.data("uniquemessage");
            if (uniquemessage !== undefined) {
                ob.rules("add", {
                    messages: {
                        remote: uniquemessage
                    }
                });
            }
            // make sure it doesn't try to check uniqueness on keyup
            ob.on("keyup", function () {
                return false;
            });
        }
    });

    $(":input[data-valid='alpha']", target).each(function () {
        $(this).rules("remove", "alpha maxlength minlength");
        var maxlength = $(this).data("maxlength");
        var maxlengthmessage = $(this).data("maxlengthmessage");
        var minlength = $(this).data("minlength");
        var minlengthmessage = $(this).data("minlengthmessage");
        $(this).rules("add", {
            alpha: true,
            maxlength: 50,
            minlength: 0,
            messages: {
                minlength: "Value is not long enough",
                maxlength: "Value is too long"
            }
        });
        if (maxlength !== undefined) {
            $(this).rules("add", {
                maxlength: maxlength
            });
        }
        if (minlength !== undefined) {
            $(this).rules("add", {
                minlength: minlength
            });
        }
        if (maxlengthmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    maxlength: maxlengthmessage
                }
            });
        }
        if (minlengthmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    minlength: minlengthmessage
                }
            });
        }
    });

    $(":input[data-valid='numeric']", target).each(function () {
        $(this).rules("remove", "number max min");
        var max = $(this).data("max");
        var maxmessage = $(this).data("maxmessage");
        var min = $(this).data("min");
        var minmessage = $(this).data("minmessage");
        $(this).rules("add", {
            number: true,
            max: 50,
            min: 0,
            messages: {
                maxmessage: "Value is not long enough",
                minmessage: "Value is too long"
            }
        });
        if (max !== undefined) {
            $(this).rules("add", {
                max: max
            });
        }
        if (min !== undefined) {
            $(this).rules("add", {
                min: min
            });
        }
        if (maxmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    maxlength: maxmessage
                }
            });
        }
        if (minmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    minlength: minmessage
                }
            });
        }
    });

    $(":input[data-valid='ukmobile'], :input[data-valid='usphonenumber'], :input[data-valid='ukphonenumber'], :input[data-valid='phonenumber']", target).each(function () {
        $(this).rules("remove", "phone maxlength");
        $(this).rules("add", {
            phone: true,
            maxlength: 30
        });
    });

    $(":input[data-valid='postcode']", target).each(function () {
        $(this).rules("remove", "postcode maxlength");
        $(this).rules("add", {
            postcode: true,
            maxlength: 8
        });
    });

    $(":input[data-valid='zipcode']", target).each(function () {
        $(this).rules("remove", "zipcode maxlength");
        $(this).rules("add", {
            zipcode: true,
            maxlength: 10
        });
    });

    $(":input[data-valid='email']", target).each(function () {
        $(this).rules("remove", "email maxlength");
        var maxlength = $(this).data("maxlength");
        var maxlengthmessage = $(this).data("maxlengthmessage");
        $(this).rules("add", {
            email: true,
            maxlength: 60,
            messages: {
                maxlength: "Max of 60 characters"
            }
        });
        if (maxlength !== undefined) {
            $(this).rules("add", {
                maxlength: maxlength
            });
        }
        if (maxlengthmessage !== undefined) {
            $(this).rules("add", {
                messages: {
                    maxlength: maxlengthmessage
                }
            });
        }
    });

    $(":input[data-valid='url']", target).each(function () {
        $(this).rules("remove", "url maxlength");
        $(this).rules("add", {
            url: true,
            maxlength: 100
        });
    });

    $(":input.con-dobfield", target).each(function () {
        $(this).rules("remove", "dob beforetoday");
        $(this).rules("add", {
            dob: true,
            beforetoday: true
        });
    });

    $(":input[data-valid='regex']", target).each(function () {
        $(this).rules("remove", "regex");
        var regex = $(this).data("regex");
        var regexmessage = $(this).data("regexmessage");
        $(this).rules("add", {
            regex: regex,
            messages: {
                regex: regexmessage
            }
        });
    });

    $(":input[data-required='required']", target).each(function () {
        $(this).rules("remove", "required");
        $(this).rules('add', {
            required: true
        });
        if ($(this).is('select')) {
            $(this).prepend('<option value="">Required</option>');
        } else {
            $(this).attr('placeholder', 'Required');
        }
    });

    $(":input[data-required='required'][data-valid='time']", target).each(function () {
        $(this).val("12:00");
    });

    $("input.con-timepicker, input.con-starttime, input.con-endtime", target).each(function () {
        $(this).rules("remove", "time");
        $(this).rules("add", {
            time: true
        });
    });

    $('input[data-datetimepair]', target).each(function () {
        $(this).rules("remove", "datetimecheck");
        $(this).rules("add", {
            datetimecheck: true
        });
    });

    $('.con-multiselect', target).on('change', function () {
        $(this).valid();
    });
}