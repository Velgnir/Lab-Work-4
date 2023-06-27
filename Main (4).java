import java.util.*;
class Node {
    // опис ноуда
    // що в ячейкі номер путь за нульом і одиницей
    // права це хай лева це лов
    public int number;
    public String line;
    public Node high;
    public Node low;

    public Node(int val, Node high, Node low,String line) {
        // функція для створення ноуда
        this.number = val;
        this.high = high;
        this.low = low;
        this.line = line;
    }


}
class BDD {

    public static String ALLel = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // всі можливі елементи

    static HashMap<String, Node> ALLNODES;
    // створюємо мапу для значень рядку(формули) і ноуда...  тобто кожній формулі відповідає лиш 1 ноуд
    public static int size=3;
    // старданий розмір починається з рута і заготовлених по стандарту кінців з 1 і 0 тому і 3 ( для себе який розмір )
    public static Node root;
    
    static Node FINALT = new Node(-1,null,null,"1");
    static Node FINALF = new Node(-1,null,null,"0");

    public BDD(Node root) {
        this.root = root;
        //обращаємся до дерева.  А ТАКОЖ ініціалізуєм мапу щоб вона існувала
       ALLNODES = new HashMap<String, Node>();
    }

    public static BDD create(String bfunction, String order) {
        // запус циклу рекурсія


        //Character.getNumericValue(c);
        root = new Node(0,null,null,bfunction);
        //создаєм ноуд якиф прокрутиму по рекурсія це наша голова
        create_rec(root,order,0); //рут це глова з якого буде розділятся, ордер це порядок зміних,ноль це рівеень щоб знать на якому росходженні
        // після цього
        BDD bdd = new BDD(root);
        //System.out.println(size);
        return bdd;

    }
    public static void create_rec(Node node, String order,int i) { // і це порядок в ордері
        if( order.length()>=i){
            // якщо не дорівнює одиниці
            //System.out.println(node.line.length());
            int j=0; // дж це коунтер0
            boolean check = false; // зміна страданто ноль вважається що ноль по дефолту а якщо є то поставм
            while(j<node.line.length()){ //від початка формули до кінця
                //System.out.println(node.line.length());
                if(node.line.charAt(j)==ALLel.charAt(Character.getNumericValue(order.charAt(i)))){ // якщо находим елемент то записуємм
                    // тру і показуєм що він є і виходим і дальшем дальше головне не ноль, ми це робим дял тогощо б лтшні раз не розділялось
                    // а тобто щоб не создавало лищніх ноудів
                    check=true;
                    break;
                }
                ++j; // це порядок елемента перебора у формулі
            }
            if(!check){ // пропскаємо що не розділять І ДОДПЄМ ОДИНИЦЮ ЩОБ ПОЗНАЧИТЬ ПЕРЕХІД НА НОВИЙ РІВЕНЬ ТА ПРОПУСК НЕ ПОТРІБНОГО КРОКУ В САМОМУ ДЕРЕВІ
                node.number++;
                create_rec(node,order,i+1); // перезапускаємо функію якщо немає і переходим запускаєм шукай елемент
            }else { // створюжмо 2 путі,
                String high_way = clean1(node.line, ALLel.charAt(Character.getNumericValue(order.charAt(i))));
                String low_way = clean2(node.line, ALLel.charAt(Character.getNumericValue(order.charAt(i))));
                //System.out.println(node.line);
                //System.out.println("H:");
               // System.out.println(i);
                boolean check_high_way = false;
                boolean check_low_way = false;
                // System.out.println(high_way);
                if(high_way =="1"){
                    check_high_way = true;
                    node.high = FINALT;
                }else if(high_way =="0" || high_way.length() == 0){
                    check_high_way = true;
                    node.high = FINALF;
                }
                
                //ЯКЩО МИ ВЖЕ ДІЙШЛИ ДО КІНЦЯ ТО МИ ЦЕ ЗАПИСУЄМО, ЩО НА ПРАВІЙ АБО ЛІВІЙ ГІЛЦІ МИ ВЖЕ НЕ ІДЕМО ДАЛІ
                if(low_way =="1"){
                    check_low_way = true;
                    node.low = FINALT;
                }else if(low_way =="0"|| low_way.length() == 0){
                    check_low_way = true;
                    node.low = FINALF;
                }
                // і це порядок і нулі це що вона пуса і нерозділяється
                if(!check_high_way && ALLNODES.get(high_way)==null){
                size += 1;
                node.high = new Node(i + 1, null, null, high_way);
                ALLNODES.put(high_way,node.high);
                create_rec(node.high, order, i + 1); // переходить нода, левела і запусає тепере від неї
                }else if(!check_high_way){
                    node.high = ALLNODES.get(high_way);
                }//ЯКЩО ЗА ФОРМУЛОЮ ЗНАЧЕННЯ НУЛЛ, А ТОБТО НЕ ІСНУЄ, МИ ІДЕМО ДАЛІ Й ДОДАЄМО НОВЕ, В ІНШОМУ ВИПАДКУ ПРОСТО ПРИСВОЮЄМУ НА ВІДПОВІДНУ ГІЛКУ ВЖЕ ІСНУЮЧУ БО ВОНИ БУДУ ОДНАКОВІ
               // System.out.println("L:");
               // System.out.println(low_way);
                if(!check_low_way&& ALLNODES.get(low_way)==null){
                size += 1; //показує по одні ячейкі створеній за раз  і створ.є ячейку яка спочатку пуста
                node.low = new Node(i + 1, null, null, low_way);
                
                ALLNODES.put(low_way,node.low);
                
                create_rec(node.low, order, i + 1);
                
                }else if(!check_low_way){
                    node.low = ALLNODES.get(low_way);
                }
                //ЯКЩО ЗА ФОРМУЛОЮ ЗНАЧЕННЯ НУЛЛ, А ТОБТО НЕ ІСНУЄ, МИ ІДЕМО ДАЛІ Й ДОДАЄМО НОВЕ, В ІНШОМУ ВИПАДКУ ПРОСТО ПРИСВОЮЄМУ НА ВІДПОВІДНУ ГІЛКУ ВЖЕ ІСНУЮЧУ БО ВОНИ БУДУ ОДНАКОВІ
                //також ПЕРЕВІРКИ ЧИ МИ НЕ ДІЙШЛИ ДО КІНЦЯ, ДЛЯ ЦЬОГО І CHECK LOW WAY
                
            }
        }
    }

    public static BDD createWithBestOrder(String bfunction,String order) { //
        // стандарнтий ордер запускаємо по ньому дерво
        String order_const=order;
        String best=order;
        Node root = new Node(0,null,null,bfunction);
        // б функція це формула , ствоємо рут і по ньому дерево
        BDD bdd = new BDD(root);
        bdd.create(bfunction,order);
        // сторюєм одерево за нашой формулою що б потім порівнювать з крашими
        int size = bdd.size;
        //записуємо його розміп

        String generate="";
        // вирізаємо з ордера рандомний символ і переносим у генерейтор


        int random_int;
        int N=order.length();
        int allcount=size;
        //System.out.println(size);
        for(int j = 0; j<N;++j){
            // за новим ордером запускаємо дерево


            generate="";


            order = order_const;// ордер конст це еталон бо вирізаємо
            for(int i=0;i<N; ++i){
                random_int = (int)Math.floor(Math.random() * (order.length()-1 - 0 + 1) + 0);// перебираємо всі елементи і перемішуємо їх
                generate += order.charAt(random_int);
                order = order.substring(0, random_int) + order.substring(random_int + 1);// сабстрінг це вирізаємо від ноля до індекса (рандом інт) поєднуємо
            }
            root = new Node(0,null,null,bfunction);
            // создаємо ще ра дерерво
            bdd = new BDD(root);
            bdd.create(bfunction,generate);
            if(bdd.size-allcount<size){ // якщо розмір ббд сайз за весь час менша то новий ставим як флажок що б потім прогресно найти найкрааший
                // в ббд сохраняється  вс я інфа  всі ячейки які сохранили, а  аллкоунт скільки було ноудів до створення  дерева
                // для якйго момента буде менше ячейок
                size = bdd.size-allcount;
                best = generate; // в бест записуєм новий ордер як найкраший
            }
            allcount = bdd.size; // скільки зробли до некст операції
        }

        root = new Node(0,null,null,bfunction);
        bdd = new BDD(root);
        bdd.create(bfunction,best);
        System.out.println(best);

        return bdd;
        // функція у неї щось просим вона поертай нам як відповідь це значення
    }


    public static String clean1(String line, char A){ //
        for(int i=0; i<line.length(); ++i){
		    //System.out.println(line.charAt(i)==A);
            if(line.charAt(i)==A){
        // перебираємо ормулу і шукаємо елемент (зміну) який потрібно змінить
                if(i>0){// якщо більше нуля а тобто не на першій позиції то начнаєи о перевірку, якщо ми знайшли елементі і він  перший
                    // то там неможу бути негації
                    if(line.charAt(i-1)!='!'){ //  якщо це не негіція то йдем в некст все удаляємо до плюсів і убираємо правий плюс бо якщо множиться все наа ноль то це і буде ноль
                        for(int j=i; j>=0; --j){// перебираємо в ліво
                            if(line.charAt(j)=='+'){// якщо плюс то закінчуємо

                                break;
                            }else{
                                line = line.substring(0, j) + ' ' + line.substring(j + 1);
                            }
                            // якщо не плюс то вирізаємо і ставим пробели щоб залишилось однакові щоб  не змістився індес рядка ( і )
                        }
                        for(int j=i; j<line.length(); ++j){// все те саме як і ліво
                            if(line.charAt(j)=='+'){
                                line = line.substring(0, j) + ' ' + line.substring(j + 1); // якщо находим то ставим пробел і вирізаємо ( в ліво ні )
                                break;
                            }else{
                                line = line.substring(0, j) + ' ' + line.substring(j + 1);
                            }
                        }
                        // це все якщо найшли елемент
                    }else{ // якщо є негація  так я кспочатку ноль то негація стає одиницею

                        if(i>1 && i<line.length()-1){if((line.charAt(i-2)=='+'||line.charAt(i-2)==' ')&&line.charAt(i-1)=='!' && line.charAt(i+1)=='+'){return  "1"; }}
                        // якщо наприкалд б буде в соло то вссе стає одиницей
                        // до першого якщо елемент не на початку і більше 1 і не є в кінці то перевіряємо перед нацією стоїть пробле чи +  значить соло
                        else if(i>1 && line.length()>1){if((line.charAt(i-2)=='+'||line.charAt(i-2)==' ')&& line.charAt(i-1)=='!'){return "1";}}
                        //не напочатку і ленз більше одиниці це означає що він не в соло, якщо не пройшов перший іф але прходить другий то значить що він в кінці немає сенсу перевіряти у право
                        else if(line.length()==2 && line.charAt(0)=='!'){return "1";}
                        // якщо розмір 2 то потрібно пеервірить тільки перший елемент якщо негація то елемент буде в 1
                        //
                        else if(i>0 && line.length()>1){if(line.charAt(i+1)=='+'&&line.charAt(i-1)=='!'){return "1";}}
                        //
                        // якщо не на  першій позиції і розмір більше 1 то потрібно перевірить чи єперед ним негація і є після лі нього плюс
                    
                        line = line.substring(0, i-1) + "  " + line.substring(i + 1);
                        // значить воно все провалило він не в соло і значить що його потрібно вирізвть бо одиниця нічого не вирішає
                    }// сабстрінг це підречення від нуля до куска і після джей це координта цього куска все сохраняємо до і після і ставимо пробеми

                }
                else {
                    //воно на першій а значить перед ним неможу бути негація
                    for(int j=i; j<line.length(); ++j){
                        if(line.charAt(j)=='+'){
                            line = line.substring(0, j) + ' ' + line.substring(j + 1);
                            // якщо не плюс то вирізаємо і ставим пробели щоб залишилось однакові щоб  не змістився індес рядка ( і )// якщо не плюс то вирізаємо і ставим пробели щоб залишилось однакові щоб  не змістився індес рядка ( і )
                            break;
                        }else{
                            line = line.substring(0, j) + ' ' + line.substring(j + 1);

                        }
                    }
                }
            }
        }
        String res = line.replace(" ","");
        // заміняє пробел на без пробеліє
        if(res.length()>0){ //убираємо  плюси тільки на правло і якщо плюс є справа то убираємо його (рес чарт А = сивол за індексом
            if(res.charAt(res.length()-1)=='+'){
             //
                res = res.substring(0, res.length()-1);
            }
        }
        return res;

    }

    public static String clean2(String line, char A){ // перший клін це предполагаємо що це ноль а другий що це один, тобто в початку предполгаємо що намісці
        //наприклад А чи є ноль чи один
        for(int i=0; i<line.length(); ++i){
            if(line.charAt(i)==A){
                if(i>0 && i<line.length()-1){if((line.charAt(i-1)=='+'||line.charAt(i-1)==' ') && line.charAt(i+1)=='+'){return "1";}}
                //якщо стандартно одиниця тоді якщо не на початку і не в кінці і в соло то вертаєм одиниця (хоч одна диниця )
                else if(i>0){if((line.charAt(i-1)=='+'||line.charAt(i-1)==' ')){return "1";}}
                //якщо не на початку а значить в кінці то перевіряємо чи злва є плюс або пробел то вертаєм один
                else if(line.length()>1){if(line.charAt(i+1)=='+'){return "1";}}else{return "1";}
                //якщо озмір більше одниці то він напочатку то перевіряємо чи справа плюс то вертаєм одиницю і якщо розмір не більше
                // одниця значить сиволо один то і буде один
                if(i>0){
                    if(line.charAt(i-1)=='!'){
                        // якщо не першом місці
                        for(int j=i; j>=0; --j){
                            if(line.charAt(j)=='+'){
                            // якщо негація стоїть перед елементом і елемент не на першому то удаляємо в ліво бкз пбсів а на прао оставляєм
                                break;
                            }else{
                                line = line.substring(0, j) + ' ' + line.substring(j + 1);
                                //зверху
                            }
                        }
                        for(int j=i; j<line.length(); ++j){
                            if(line.charAt(j)=='+'){
                                line = line.substring(0, j) + ' ' + line.substring(j + 1);
                                break;
                            }else{
                                line = line.substring(0, j) + ' ' + line.substring(j + 1);
                            }
                            //це якщо направо
                        }
                    }else{
                        line = line.substring(0, i) + ' ' + line.substring(i + 1);
                    }
                    //якщо нема негація то удаляєм елемент
                }else{
                    line =   line.substring( 1);
                }
                    // вона не в соло і пройшла тести то вона 1 і мало що вирішує зміна ноль (наприкалд а )
            }
        }
        String res = line.replace(" ","");
        if(res.length()>0){
            if(res.charAt(res.length()-1)=='+'){
                //щоб елемент був непплюсом
                res = res.substring(0, res.length()-1);
            //оставляєм все від нля до кінця
            }
        }

        return res;
    }

    public char use(String inputValues) {
        Node copy = root; // це вже все дерео копіюємо рут
        int i=0;
        //System.out.println(copy.line);
        while(i<inputValues.length()){
            //System.out.println(copy.line);
            

            if(inputValues.charAt(i)=='1'){
                copy = copy.low;
                
                // юзаємо лівий це один
            }else{
                
                copy = copy.high;
                
                //юзаємо правий це ноль
            }
             
            if(copy.line=="1"||copy.line=="0"){
                // якщо наш ячейка дорівнює 1 бабо 0 ( значить фінал ) то завершаємо і виводимо
                break;
            }
            
            i=copy.number;
            //++i;
        }
        System.out.println(copy.line);
        return (copy.line).charAt(0);
                //якщо один чар в стрінгі  то і забираємо за єдиним індексом (єдина це нольбо починається від нуля )
    }


}

public class Main
{
    public static void main(String[] args) {

        Node root = new Node(0,null,null,"A+BC");
        // для кажного нода показать порядковий номер а тобто висота ййого діти це лов і хай
        BDD bdd = new BDD(root);

        bdd.create("!A+!B!C+AD","0123");
        //System.out.println(bdd.size);
       // bdd.createWithBestOrder("A+BC","0123");

        bdd.use("1011");
    }
}