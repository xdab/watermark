
# watermark

Program stworzony na przedmiot obieralny *Zaawansowane programowanie obiektowe i funkcyjne*, sem. 5 studiów informatycznych na wydziale MiNI Politechniki Warszawskiej.


## Pobranie

Link do pre-release z końca 31 stycznia:
https://github.com/xdab/watermark/releases/download/1.0-rc1/watermark-1.0-rc1.jar

Plik .jar jest samowystarczalny, tzn. zawiera wszystkie zależności oraz pliki .properties (załadowany język angielski, konfiguracja loggera do debugowania).

Link do przykładowego obrazu:
https://github.com/xdab/watermark/raw/master/testInput1.png

## Interfejs użytkownika

Program ma interfejs konsolowy. Informacje o parametrach wywołania można zdobyć próbą uruchomienia bez (właściwych) parametrów.

Wbudowane "*readme*" załączone poniżej:

    usage: watermark
     -r                      Read existing watermark          (out: STDOUT)
     -w                      Write new watermark to the input (out: image)
     -i,--input <FILE>       Image file to be watermarked
     -o,--output <FILE>      Resulting image file name    (def.: /auto/)
     -m,--message <STRING>   Watermark message            (def.: WATERMARK)
     -t,--type <NAME>        Type of watermark method     (def.: LSB)
     -n,--number <NUMBER>    Repetitions of watermark     (def.: 1)
     -k,--key <NUMBER>       Seed for random generator    (def.: /random/)
     -h                      Flag: Horizontal (where applicable)
     -v                      Flag: Vertical   (where applicable)
     -M                      Flag: Majority   (where applicable)
     -V                      Generate images visualizing input & output LSBs
     -l                      Alias: --type=LSB
     -s                      Alias: --type=Stripes
     -c                      Alias: --type=Constellation (WIP)


## Parametry

Podstawowym parametrem działania programu jest flaga ***r**ead*/***w**rite*. 
W trybie czytania program będzie próbował odczytać znak wodny z obrazu wejściowego i wypisać go na *stdout*.
W trybie pisania nastąpi wczytanie obrazu wejściowego, zapisanie na nim znaku wodnego oraz zapisanie obrazu wynikowego pod wskazaną (bądź automatyczną) nazwą.

Niezbędnymi argumentami są ***i**nput* i ***t**ype*. Input określa ścieżkę obrazu wejściowego. Type określa rodzaj stosowanego algorytmu.
Aktualnie obsługiwane algorytmy:

    lsb stripes

Użytkownik może podać własną ścieżkę dla obrazu wynikowego przez parametr ***o**utput*.  Domyślnie będzie to nazwa obrazu wynikowego z dopiskiem "*_out*".

W algorytmach rozróżniających między przetwarzaniem obrazu przy pomocy linii pionowych a poziomych, można określić ten porządek jedną z flag ***h**orizontal*/***v**ertical*.

Przy zapisywaniu wiadomości należałoby podać jej treść (w przeciwnym przypadku zostanie zapisana wiadomość domyślna = "*MESSAGE*").
Służy do tego opcja ***m**essage*. Wiadomości zawierające spacje należy podawać w cudzysłowie.

Wiadomość można zapisywać kilkakrotnie (domyślnie jednorazowo).
Liczbę powtórzeń wiadomości określa opcja **n**umber.

Jeśli algorytm zapisuje jeden bit wiadomości na kilku bitach obrazu, flagą **M** (od ang. *majority*) można zdecydować, czy jedynkę koduje całość jedynek w bloku zapisu bitu, czy większość.

Flaga **V** (od ang. *visualize*) zleca stworzenie wizualizacji ukazujących ostatnie bity przetwarzanego obrazu (bądź wejścia i wyjścia). Ich nazwy odpowiadają wejściu (i wyjściu) z dopiskiem "*_LSB*".

Parametr ***k**ey* nie ma znaczenia.

## Przykłady użycia

Oznakowanie pliku "*testInput1.png*" wiadomością "*TEST*", algorytmem prostym.
Wynik zostanie zapisany do pliku "*out.png*".

    java -jar watermark-1.0-rc1.jar -w -i testInput1.png -o out.png -t lsb -m TEST

Odczytanie wiadomości zapisanych algorytmem prostym, zawartch w pliku "*out.png*".

    java -jar watermark-1.0-rc1.jar -r -i out.png

Przykłady z dodatkowymi flagami:

    java -jar watermark-1.0-rc1.jar -w -i testInput1.png -o out2.png -t stripes -m "Majority Visualize" -M -V
    java -jar watermark-1.0-rc1.jar -r -i out3.png -t stripes -m "Majority Horizontal" -M -h

## Kodowanie

Ponieważ liczba kanałów kolorów piksela (3) nie dzieli równo liczby bitów w bajcie, do każdego bajtu dodawany jest bit parzystości. Tak uzyskane 9 bitów mieści się równo w 3 jednostkach zapisu (pikselach, bądź blokach pikseli).
Dodatkową zaletą jest sprawniejsze odrzucanie niewłaściwych wiadomości.

Na początku wiadomości zapisywane jest dodatkowo słowo synchronizacji (*0x5b5b*).
Na końcu: słowo kończące (*0x2424*). *0x5b* w tablicy ASCII odpowiada znakowi '$', *0x24* odpowiada '['.


## Metoda LSB

Zapis/odczyt najmniej znaczących bitów w kolejnych pikselach obrazu. Począwszy od losowej pozycji początkowej. Kierunek postępu określa flaga (domyślnie od lewej do prawej, od góry do dołu)

## Metoda Stripes

Jeden pasek odpowiada jednemu kanałowi koloru w jednej całej linii pikseli (pion/poziom określa flaga). Jedna linia składa się wobec tego z trzech pasków.

Bit wiadomości przenoszony jest na ostatnich bitach wartości w pasku.
Bez flagi **M**, wszystkie wartości muszą być zgodnie ustawione na bit wiadomości.
Z flagą wystarczy większość.
