/**
 * This content is released under the MIT License (MIT)
 *
 * Copyright (c) 2018, canchito-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author 		Jose Carlos Mendoza Prego
 * @copyright	Copyright (c) 2018, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/canchito-workflow-manager
 **/
Date.prototype.addSeconds = function(seconds) {
	this.setSeconds(this.getSeconds() + seconds);
	return this;
};

Date.prototype.addMinutes = function(minutes) {
	this.setMinutes(this.getMinutes() + minutes);
    return this;
};

Date.prototype.addHours = function(hours) {
	this.setHours(this.getHours() + hours);
    return this;
};

Date.prototype.addDays = function(days) {
    this.setDate(this.getDate() + days);
    return this;
};

Date.prototype.addWeeks = function(weeks) {
    this.addDays(weeks*7);
    return this;
};

Date.prototype.addMonths = function (months) {
    var dt = this.getDate();

    this.setMonth(this.getMonth() + months);
    var currDt = this.getDate();

    if (dt !== currDt) {  
    	this.addDays(-currDt);
    }

    return this;
};

Date.prototype.addYears = function(years) {
    var dt = this.getDate();

    this.setFullYear(this.getFullYear() + years);

    var currDt = this.getDate();

    if (dt !== currDt) {  
    	this.addDays(-currDt);
    }

    return this;
};

Date.prototype.getWeek = function(date) {
	var d = new Date(+date);
    d.setHours(0, 0, 0);
    d.setDate(d.getDate() + 4 - (d.getDay() || 7));
    return Math.ceil((((d - new Date(d.getFullYear(), 0, 1)) / 8.64e7) + 1) / 7);
};

Date.prototype.getYear = function(date) {
	if(date != null && date != '')
		date = new Date(date);
	else
		date = Date.now();
	return date.getFullYear();
};

Date.prototype.getMonth = function(date) {
	if(date != null && date != '')
		date = new Date(date);
	else
		date = Date.now();
	return date.getMonth();
};

Date.prototype.getMonthYear = function(date) {
	if(date != null && date != '')
		date = new Date(date);
	else
		date = Date.now();

	var month = date.getMonth() + 1;
	month = month < 10 ? '0' + month : month;
	
	return date.getFullYear() + '-' + month;
};