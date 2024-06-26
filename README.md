<h1>OpusReaderApp</h1>
<span>
  This project is an Android application which purpose is to show all the content of anyone's <a href="https://www.artm.quebec/tarification/support-de-titres/opus/" target="_blank">Opus</a> or Occasional cards in a nice interface.
  It allows the user to see the fares and the trips made that are on their cards. Therefore, it allows users to better utilize their transit fares by having information about when the validity started and what lines to avoid in order to respect transfer rules in place.
  It provides knowledge on all the data that is being stored on their card too.
</span>
<h2>Project state</h2>
<span>
  Currently, as this project is still in development, a lot of fares, operators and lines are missing and won't be recognize.
</span>
<br>
<h4>Partially working for the following agencies</h4>
<ul>
  <li>exo (Trains and Laurentides sector only)</li>
  <li>REM (Réseau express métropolitain)</li>
  <li>RTL (Réseau de transport de Longueuil)</li>
  <li>STM (Société de transport de Montréal)</li>
  <li>STL (Société de transport de Laval)</li>
</ul>
<h4>Available in the following languages</h4>
<ul>
  <li>English</li>
  <li>Français</li>
</ul>
<h2>User interface</h2>
<span>
  The application is using a layout similar to the one of <a href="https://play.google.com/store/apps/details?id=quebec.artm.chrono" target="_blank">Chrono</a> card reader module but adding missing information like the buying date or the last trips section.
  It is availible in both light and dark modes matching the system default.
</span>
<br>
<br>
<table>
  <tr>
    <td>Opus Card fares view in light mode</td>
    <td>Opus Card trips view in light mode</td>
    <td>Occasional Card view in light mode</td>
  </tr>
  <tr>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/4482b09c-7d95-43c3-b001-c81e22d6d989" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/cd0c4a77-b75f-4006-b93c-391ed2b56f77" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/e52fffcf-1359-45c6-a9ac-5302303034da" width=270></td>
  </tr>
</table>
<br>
<table>
  <tr>
    <td>Opus Card fares view in dark mode</td>
    <td>Opus Card trips view in dark mode</td>
    <td>Occasional Card view in dark mode</td>
  </tr>
  <tr>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/4434d0aa-87db-40f7-b32c-3772301d8cf1" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/a84b6844-99b3-4f2e-bed0-dfe8d6c8d0f1" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/a5fbc974-a7f6-4bc8-9288-b903f3e0dc35" width=270></td>
  </tr>
</table>
<br>
<span>
  An history of all the scans that have been made is also available to users. The history is sorted by the latest scan date of a specific card and by clicking on it all scans are listed. 
  All the data is stored on device using the <a href="https://developer.android.com/training/data-storage/room" target="_blank">RoomDatabase</a> provided by Android.
</span>
<br>
<br>
<table>
  <tr>
    <td>History access from main view</td>
    <td>History view in light mode</td>
    <td>History view in dark mode</td>
  </tr>
  <tr>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/5c265e55-fef5-47a8-a949-00b8fad2ba1c" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/6d771217-d257-46e1-b337-1177628c8637" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/ea027d94-e513-47b1-b18e-37225c29ccb8" width=270></td>
  </tr>
</table>
<h2>How to use</h2>
<span>
  Make sure your device has NFC capailities and that you have enabled them. Then, you simply have to open the app, scan a compatible card and all the information from the card will display.
  As stated above, some fares, operators and lines are missing and they will show as unknown for the moment. On the content view, you can use Android's back gesture to scan another card.
</span>
<h2>Upcoming improvements</h2>
<ul>
  <li>Adding the possibility for user to crowdsource unknown fares, lines and operators</li>
  <li><strike>Improving information for transfers so users can maximize their usage of one particular fare</strike> <em>Added!</em></li>
  <li>Adding notifications to alert the user when the validity of the fare ends and few minutes prior</li>
  <li><strike>Adding an history of the card scans</strike> <em>Added!</em></li>
</ul>
<h2>Documentation</h2>
<span>
  In addition to some time analysing the binary content of the cards, here are some links that were used to find how to retreive and parse data from both of the cards.
</span>
<ul>
  <br>
  <li><a href="https://www.nxp.com/docs/en/data-sheet/MF0ICU1.pdf">MiFare Ultralight</a> (Occasional Card)</li>
  <li><a href="https://calypsonet.org/" target="_blank">Calypso</a> [Calypso Prime Spec] (Opus Card)</li>
  <li><a href="https://github.com/etiennedub/LecteurOPUS?tab=readme-ov-file" target="_blank">LecteurOPUS</a> (both Opus and Occasional Card)</li>
  <li><a href="https://github.com/metrodroid/metrodroid/tree/bbd61960b260c314a2c891bd0d54ac96ae654c16" target="_blank">Metrodroid</a> (both Opus and Occasional Card)</li>
</ul>
