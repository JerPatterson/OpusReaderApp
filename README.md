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
  <li>RTL (Réseau de transport de Longueuil)</li>
  <li>STM (Société de transport de Montréal)</li>
  <li>STL (Société de transport de Laval)</li>
</ul>
<h4>Availible in the following languages</h4>
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
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/96bbdaa6-3aae-44c9-a0a2-03b852041866" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/37d6d670-4748-4cbf-be0f-9ba544eb5268" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/9ffaf218-44c4-44b0-b259-b999badb6a91" width=270></td>
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
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/7de74b5d-5e68-4cc8-82f2-16d13a6712a9" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/0baa42a2-a346-48a4-8844-28c0aeed73b7" width=270></td>
    <td><img src="https://github.com/JerPatterson/OpusReaderApp/assets/89818093/6aa4217c-2f41-44a3-a46a-5b70cd8e8aa9" width=270></td>
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
  <li>Adding information for transfers so users can maximize their usage of one particular fare</li>
  <li>Adding an historic of the card scans</li>
</ul>
<h2>Documentation</h2>
<span>
  In addition to some time analysing the binary content of the cards, here are some link that were used to find how to retreive and parse data from both of the cards.
</span>
<ul>
  <br>
  <li><a href="https://www.nxp.com/docs/en/data-sheet/MF0ICU1.pdf">MiFare Ultralight</a> (Occasional Card)</li>
  <li><a href="https://calypsonet.org/" target="_blank">Calypso</a> [Calypso Prime Spec] (Opus Card)</li>
  <li><a href="https://github.com/etiennedub/LecteurOPUS?tab=readme-ov-file" target="_blank">LecteurOPUS</a> (both Opus and Occasional Card)</li>
  <li><a href="https://github.com/metrodroid/metrodroid/tree/bbd61960b260c314a2c891bd0d54ac96ae654c16" target="_blank">Metrodroid</a> (both Opus and Occasional Card)</li>
</ul>
