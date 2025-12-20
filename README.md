**Sistema de gerenciamento de um banco:**<br>
Projeto de integração de Java com banco de dados SQL.<br>
Os códigos SQL estão no arquivo dbprojetobancosql.sql<br>

**Arquitetura:**<br>
Linguagens: Java, SQL (banco) e CSS (design)<br>
IDE: IntelliJ IDEA<br>
Interfaces: JavaFX<br>
Criação de .fxml para interfaces: Scene Builder<br>
Relatórios: Jasper Studio<br>
ícones foram pegos no Flaticon<br>

**Banco de dados:**<br>
SGBD: PostgreSQL<br>
Conceitual/Lógico: BrModelo<br>

**Vídeo de apresentação (mostra algumas classes e funções):** *https://drive.google.com/file/d/1shPHOC5YgYTCkasCTDho65PC8F8VFewu/view?usp=sharing* <br>

**Capturas de tela:**<br>

<img width="701" height="494" alt="image" src="https://github.com/user-attachments/assets/4f2b511e-4123-4b39-856c-4639a8b121b2" />
<img width="1039" height="648" alt="image" src="https://github.com/user-attachments/assets/1dfbff94-c900-48ed-ac1f-4ad2d079ab30" />
<img width="1039" height="650" alt="image" src="https://github.com/user-attachments/assets/38c7b084-00c9-4ce5-a72a-cb3deb6a65b1" />
<img width="1040" height="650" alt="image" src="https://github.com/user-attachments/assets/00420e08-fea6-457b-9750-e566031d2c67" />
<img width="1038" height="650" alt="image" src="https://github.com/user-attachments/assets/8f059e95-9a2c-436c-b762-ef377e304eb0" />
<img width="1037" height="649" alt="image" src="https://github.com/user-attachments/assets/f9d03862-e5a7-455f-80d1-699915e7b1c1" />
<img width="1037" height="650" alt="image" src="https://github.com/user-attachments/assets/db220d8e-a911-4551-ae52-2eafbae06743" />

**Exemplos de criação de views com o Scene Builder:** <br>

<img width="1320" height="796" alt="image" src="https://github.com/user-attachments/assets/d7bf496b-b2e6-4604-9d26-8cec5a2881b8" />
<img width="1382" height="856" alt="image" src="https://github.com/user-attachments/assets/bd802de4-3b5f-483d-8da9-833c3de4001b" />
<img width="1396" height="766" alt="image" src="https://github.com/user-attachments/assets/5a4e995c-8952-4ec7-903b-e3aca908fcbd" />

**Exemplo de .css (estilização da tela de login):**<br>

```css
.text-field, .password-field {
    -fx-background-color: #dadae6;
    -fx-background-insets: 0, 1.5;
    -fx-background-radius: 10px;
    -fx-border-radius: 10px;
    -fx-text-fill: #0c0c0c;
    -fx-prompt-text-fill: rgba(131, 135, 149, 0.97);
    -fx-font-size: 14px;
}

.text-field:hover, .password-field:hover {
    -fx-border-radius: 10px;
    -fx-background-color: #eceff4;
    -fx-effect: dropshadow(gaussian, rgba(0, 200, 255, 0.34), 8, 0.1, 0, 0);
}

.text-field:focused, .password-field:focused {
    -fx-border-color: transparent, linear-gradient(from 0% 0% to 100% 100%, #3a89ff, #00eaff);
    -fx-border-width: 2px;
    -fx-border-radius: 10px;
    -fx-background-color: #e2e7ec;
}

.button {
    -fx-background-color: linear-gradient(to left bottom, #66c4ea, #3e65b8);
    -fx-font-weight: bold;
    -fx-background-radius: 14px;
    -fx-text-fill: white;
    -fx-border-color: transparent;
    -fx-font-size: 16px;
}

.button:hover {
    -fx-background-color: linear-gradient(to left bottom, #84cee6, #567cc8);
    -fx-cursor: hand;
}

.button:pressed {
    -fx-background-color: white;
    -fx-text-fill: #111112;
    -fx-border-width: 2px;
    -fx-border-color: linear-gradient(to left bottom, #66c4ea, #3e65b8);;
    -fx-border-radius: 14px;
    -fx-effect: dropshadow(gaussian, rgba(66, 216, 255, 0.57), 15, 0.15, 0, 0);
}

.check-box {
    -fx-text-fill: #212844;
    -fx-font-size: 14px;
    -fx-padding: 4 0 4 0;
}

.check-box .box {
    -fx-background-color: #eef5f8;
    -fx-border-color: #557cd3;
    -fx-border-radius: 4;
    -fx-background-radius: 4;
    -fx-border-width: 1.5;
    -fx-cursor: hand;
}

.check-box:hover .box {
    -fx-background-color: #f0f0ff;
    -fx-border-color: #a7d6ea;
}

.check-box:selected .mark {
    -fx-background-color: white;
    -fx-shape: "M3 7 L6 10 L13 3 L15 5 L6 14 L1 9 Z";
    -fx-scale-x: 1;
    -fx-scale-y: 1;
}

.check-box:selected .box {
    -fx-border-color: transparent;
    -fx-background-color: #2573cd;
}
```
