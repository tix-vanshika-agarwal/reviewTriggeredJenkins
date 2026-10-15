package com.tiket.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Groups test results by {@code @Tribe} and injects a "Tribe" tab into the Spark report. */
public final class TribeReportView {

    private TribeReportView() {
    }

    private record Entry(int id, String name, String status, String time) {
    }

    private static final Map<String, List<Entry>> DATA = new LinkedHashMap<>();

    public static synchronized void record(String tribe, int id, String name, String status, long startMillis) {
        if (tribe == null || tribe.isBlank()) {
            return;
        }
        String time = new SimpleDateFormat("HH:mm:ss a").format(new Date(startMillis));
        DATA.computeIfAbsent(tribe.trim(), k -> new ArrayList<>())
                .add(new Entry(id, name == null ? "" : name, normalizeStatus(status), time));
    }

    public static synchronized boolean hasData() {
        return !DATA.isEmpty();
    }

    public static synchronized void reset() {
        DATA.clear();
    }

    public static synchronized String buildJs() {
        return "(function(){var TRIBE_DATA=" + buildJson() + ";" + JS_BODY + "})();";
    }

    private static String normalizeStatus(String status) {
        if (status == null) {
            return "pass";
        }
        String s = status.trim().toLowerCase();
        if (s.startsWith("fail")) {
            return "fail";
        }
        if (s.startsWith("skip")) {
            return "skip";
        }
        return "pass";
    }

    private static String buildJson() {
        StringBuilder sb = new StringBuilder("[");
        boolean firstTribe = true;
        for (Map.Entry<String, List<Entry>> tribe : DATA.entrySet()) {
            if (!firstTribe) {
                sb.append(',');
            }
            firstTribe = false;
            sb.append("{\"tribe\":\"").append(escape(tribe.getKey())).append("\",\"tests\":[");
            boolean firstTest = true;
            for (Entry e : tribe.getValue()) {
                if (!firstTest) {
                    sb.append(',');
                }
                firstTest = false;
                sb.append("{\"id\":").append(e.id())
                        .append(",\"name\":\"").append(escape(e.name())).append('"')
                        .append(",\"status\":\"").append(e.status()).append('"')
                        .append(",\"time\":\"").append(escape(e.time())).append("\"}");
            }
            sb.append("]}");
        }
        sb.append(']');
        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\' -> b.append("\\\\");
                case '"' -> b.append("\\\"");
                case '\n' -> b.append("\\n");
                case '\r' -> b.append("\\r");
                case '\t' -> b.append("\\t");
                case '<' -> b.append("\\u003c");
                case '>' -> b.append("\\u003e");
                case '&' -> b.append("\\u0026");
                case '\u2028' -> b.append("\\u2028");
                case '\u2029' -> b.append("\\u2029");
                default -> {
                    if (c < 0x20) {
                        b.append(String.format("\\u%04x", (int) c));
                    } else {
                        b.append(c);
                    }
                }
            }
        }
        return b.toString();
    }

    private static final String JS_BODY = """
            function esc(s){return (''+s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');}
            function buildTribeView(){
              var $=window.jQuery; if(!$) return;
              if($('#nav-tribe').length) return;
              if(!$('.main-content').length || typeof TRIBE_DATA==='undefined' || !TRIBE_DATA.length) return;

              var $nav=$('<li class="nav-item dropdown"><a id="nav-tribe" class="dropdown-toggle" href="#" title="Tribe"><span class="ico"><i class="fa fa-users"></i></span></a></li>');
              $nav.on('click',function(){ toggleView('tribe-view'); });
              var $dash=$('#nav-dashboard').closest('li');
              if($dash.length){ $dash.before($nav); } else { $('.side-nav-menu').append($nav); }

              var items='';
              for(var i=0;i<TRIBE_DATA.length;i++){
                var t=TRIBE_DATA[i], tests=t.tests||[], p=0, f=0, s=0, rows='';
                for(var j=0;j<tests.length;j++){
                  var te=tests[j], st=te.status;
                  if(st==='fail'){f++;} else if(st==='skip'){s++;} else {p++;}
                  var label=(st==='fail')?'Fail':((st==='skip')?'Skip':'Pass');
                  rows+='<tr class="tag-test-status" status="'+st+'"><td><span class="badge log '+st+'-bg">'+label+'</span></td><td>'+esc(te.time)+'</td><td><a href="#" class="linked" test-id="'+te.id+'" id="'+te.id+'">'+esc(te.name)+'</a></td></tr>';
                }
                var total=tests.length, meta='', info='';
                if(p>0){ meta+='<span status="pass" role="button" tabindex="0" aria-label="Filter by passed" class="badge log pass-bg" style="cursor:pointer;margin-right:6px">'+p+'</span>'; info+='<span status="pass" role="button" tabindex="0" aria-label="Filter by passed" class="badge log pass-bg" style="cursor:pointer;margin-right:8px">'+p+' passed</span>'; }
                if(f>0){ meta+='<span status="fail" role="button" tabindex="0" aria-label="Filter by failed" class="badge log badge-danger" style="cursor:pointer;margin-right:6px">'+f+'</span>'; info+='<span status="fail" role="button" tabindex="0" aria-label="Filter by failed" class="badge log badge-danger" style="cursor:pointer;margin-right:8px">'+f+' failed</span>'; }
                if(s>0){ meta+='<span status="skip" role="button" tabindex="0" aria-label="Filter by skipped" class="badge log badge-skip" style="cursor:pointer;margin-right:6px">'+s+'</span>'; info+='<span status="skip" role="button" tabindex="0" aria-label="Filter by skipped" class="badge log badge-skip" style="cursor:pointer;margin-right:8px">'+s+' skipped</span>'; }
                items+='<li class="test-item"><div class="test-detail"><span class="meta">'+meta+'</span><p class="name">'+esc(t.tribe)+'</p><p class="duration text-sm">'+total+' tests</p></div><div class="test-contents d-none"><div class="info"><h4>'+esc(t.tribe)+'</h4>'+info+'</div><table class="table table-sm mt-4"><thead><tr><th class="status-col">Status</th><th class="timestamp-col">Timestamp</th><th>TestName</th></tr></thead><tbody>'+rows+'</tbody></table></div></li>';
              }

              var panel='<div class="test-wrapper row view tribe-view attributes-view d-none"><div class="test-list"><div class="test-list-tools"><ul class="tools pull-left"><li><a href=""><span class="font-size-14">Tribe</span></a></li></ul><ul class="tools text-right"><li><a href="#"><span class="badge badge-primary">'+TRIBE_DATA.length+'</span></a></li></ul></div><div class="test-list-wrapper scrollable"><ul class="test-list-item">'+items+'</ul></div></div><div class="test-content scrollable"><div class="test-content-detail"><div class="detail-body"></div></div></div></div>';
              $('.main-content').append(panel);

              function filterTribe(detail, st){
                var rows=detail.find('.tag-test-status'), chips=detail.find('.info > span[status]');
                if(detail.data('tribeFilter')===st){ rows.removeClass('d-none'); chips.css('opacity',''); detail.removeData('tribeFilter'); return; }
                rows.addClass('d-none').filter(function(){ return $(this).attr('status')===st; }).removeClass('d-none');
                chips.css('opacity','0.4').filter(function(){ return $(this).attr('status')===st; }).css('opacity','');
                detail.data('tribeFilter', st);
              }
              function selectTribe(item){
                $('.tribe-view .test-item').removeClass('active');
                var content=item.addClass('active').find('.test-contents').clone();
                var detail=$('.tribe-view .test-content-detail');
                detail.removeData('tribeFilter').find('.detail-body').empty().append(content.removeClass('d-none'));
                detail.find('.info > span[status]').css('opacity','');
                return detail;
              }
              $(document).on('click','.tribe-view .test-item',function(e){
                if($(e.target).closest('.meta span[status]').length) return;
                selectTribe($(this));
              });
              $(document).on('click','.tribe-view .test-detail .meta span[status]',function(){
                var st=$(this).attr('status'), item=$(this).closest('.test-item'), detail=$('.tribe-view .test-content-detail');
                if(!item.hasClass('active')){ detail=selectTribe(item); }
                filterTribe(detail, st);
              });
              $(document).on('click','.tribe-view .test-content-detail .info > span[status]',function(){
                filterTribe($(this).closest('.test-content-detail'), $(this).attr('status'));
              });
              $(document).on('keydown','.tribe-view span[status][role="button"]',function(e){
                if(e.key==='Enter'||e.key===' '||e.keyCode===13||e.keyCode===32){ e.preventDefault(); $(this).click(); }
              });
              $(document).on('click','.tribe-view .test-content .linked',function(e){
                e.preventDefault();
                // Strip non-digits: Spark renders test-id with thousands-separators (e.g. "1,015" or "1,234,567")
                // for large ids, so compare on digits-only to keep the link working regardless of id size.
                var testId=($(this).attr('test-id')||'').replace(/[^0-9]/g,'');
                $('#nav-test').click();
                $('.test-item').filter(function(){ return ($(this).attr('test-id')||'').replace(/[^0-9]/g,'')===testId; }).click();
              });
            }
            if(window.jQuery){ window.jQuery(function(){ buildTribeView(); }); }
            else { document.addEventListener('DOMContentLoaded', buildTribeView); }
            """;
}
