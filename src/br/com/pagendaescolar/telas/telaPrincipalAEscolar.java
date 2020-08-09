/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pagendaescolar.telas;

import br.com.pagendaescolar.dal.ConexaoAEscolar;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author franz
 */
public class telaPrincipalAEscolar extends javax.swing.JFrame {

    //cria tres variaveis preparando
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form telaPrincipal
     */
    public telaPrincipalAEscolar() {
       
        initComponents();
        atualiza_datahoje();
        carregar_tabela_eventos();

    }

    private void pesquisar_eventos() {

        Statement stmt = null;
        String evento, materia, descricao, data, datalimite, status;

        status = "'" + comboStatus.getSelectedItem().toString() + "'";

        String sql = "SELECT * FROM tbl_enventos WHERE status_evento = " + status + "ORDER BY data_limite";

    }

    private void atualiza_datacalendario() {

        Date data = jCalendario.getDate();

        String strData = DateFormat.getDateInstance().format(data);

        lblDataCalendario.setText(strData);

    }

    private void atualiza_datahoje() {

        //aqui código para data de hoje no lblDataHoje
        // substitui a label pela data atual
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        //essa classe já formata em string

        lblDataHoje.setText(formatador.format(data));

    }

    private void adicionar_evento() {

        Statement stmt = null;
        String evento, materia, descricao, data, datalimite, status;

        evento = txtEvento.getText();
        materia = comboMateria.getSelectedItem().toString();
        descricao = txtDescricao.getText();
        data = lblDataCalendario.getText();
        datalimite = lblDataLimite.getText();
        status = comboStatus.getSelectedItem().toString();

        String sql = "INSERT INTO tbl_eventos(nome_evento,disciplina_evento,descricao_evento,data_evento,data_limite,status_evento) VALUES(" + "'" + evento + "'" + "," + "'" + materia + "'" + "," + "'" + descricao + "'" + "," + "'" + data + "'" + "," + "'" + datalimite + "'" + "," + "'" + status + "'" + ")";

        System.out.println(sql);

        try {
            conexao = ConexaoAEscolar.conector();
            conexao.setAutoCommit(false);
            stmt = (Statement) conexao.createStatement();

            if ((txtEvento.getText().isEmpty()) || (txtDescricao.getText().isEmpty())) {

                JOptionPane.showMessageDialog(null, "Todos os campos são obrigatórios!");
            } else {

                //a estrutura abixo é usada para confirmar a inserção dos dados na tabela
                int adicionado = stmt.executeUpdate(sql);

                stmt.close();
                conexao.commit();
                conexao.close();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Evento cadastrado com sucesso!");

                    txtEvento.setText("Evento");
                    txtDescricao.setText("Breve descrição");

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro no cadastro de evento: " + e);
        }

    }

    private void alterar_evento() {
        
        
        //a estrutura abixo confirma a remoção do usuário
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja alterar este evento?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {

            Statement stmt = null;

            String evento, materia, descricao, data, datalimite, status;
            String idevento;

            idevento = txtId.getText();

            evento = txtEvento.getText();
            materia = comboMateria.getSelectedItem().toString();
            descricao = txtDescricao.getText();
            data = lblDataCalendario.getText();
            datalimite = lblDataLimite.getText();
            status = comboStatus.getSelectedItem().toString();
            
            

            String sql = "UPDATE tbl_eventos SET nome_evento = " + "'" + evento + "'" + ", disciplina_evento= " + "'" + materia + "'" + ", descricao_evento= " + "'" + descricao + "'" + ", data_evento= " + "'" + data + "'" + ", data_limite= " + "'" + datalimite + "'" + ", status_evento= " + "'" + status + "'" + " WHERE id_evento=" + idevento;

            System.out.println(sql);

            try {

                conexao = ConexaoAEscolar.conector();
                conexao.setAutoCommit(false);
                stmt = (Statement) conexao.createStatement();

                if ((txtEvento.getText().isEmpty()) || (txtDescricao.getText().isEmpty())) {

                    JOptionPane.showMessageDialog(null, "Todos os campos são obrigatórios!");
                } else {

                    //a estrutura abixo é usada para confirmar a inserção dos dados na tabela
                    int adicionado = stmt.executeUpdate(sql);

                    stmt.close();
                    conexao.commit();
                    conexao.close();

                    if (adicionado > 0) {
                        JOptionPane.showMessageDialog(null, "Evento alterado com sucesso!");

                        txtEvento.setText("Evento");
                        txtDescricao.setText("Breve descrição");

                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na alteração: " + e);
            }
        }

    }

    private void setar_campos() {
        
        conexao = ConexaoAEscolar.conector();
            

        int setar = tblEventos.getSelectedRow();
        txtId.setText(tblEventos.getModel().getValueAt(setar, 0).toString());
        
       

    }

    private void pesquisar_evento_id() {

        Integer idevento = Integer.parseInt(txtId.getText());

        try {

            String sql = "SELECT id_evento, nome_evento, disciplina_evento, descricao_evento, data_evento, data_limite, status_evento FROM tbl_eventos WHERE id_evento = " + idevento;

            try {

                pst = conexao.prepareStatement(sql);

                rs = pst.executeQuery();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Erro na conexao: " + e);
            }

            txtId.setText(rs.getString(1));
            txtEvento.setText(rs.getString(2));
            comboMateria.setSelectedItem(rs.getString(3));
            txtDescricao.setText(rs.getString(4));
            lblDataCalendario.setText(rs.getString(5));
            lblDataLimite.setText(rs.getString(6));
            comboStatus.setSelectedItem(rs.getString(7));
            
            conexao.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro na pesquisa: " + e);
        }

    }

    private void carregar_tabela_eventos() {

        try {

            String sqltabela = "SELECT * FROM tbl_eventos";

            //Statement stmt = null;
            conexao = ConexaoAEscolar.conector();
            conexao.setAutoCommit(false);

            pst = conexao.prepareStatement(sqltabela);

            rs = pst.executeQuery();

            System.out.println(rs);

//            stmt = (Statement) conexao.createStatement();
//            
//            rs = stmt.executeQuery(sqltabela);
            tblEventos.setModel(DbUtils.resultSetToTableModel(rs));
            
            pst = null;
            rs = null;
            
            conexao.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jCalendario = new com.toedter.calendar.JCalendar();
        jLabel1 = new javax.swing.JLabel();
        txtEvento = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescricao = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        btnAdicionar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEventos = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        comboMateria = new javax.swing.JComboBox<>();
        lblUsuario = new java.awt.Label();
        jDataLimite = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        lblDataLimite = new javax.swing.JLabel();
        comboStatus = new javax.swing.JComboBox<>();
        lblDataCalendario = new java.awt.Label();
        lblDataHoje = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AGENDA ESCOLAR");

        jPanel1.setBackground(new java.awt.Color(0, 51, 0));

        jCalendario.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jCalendario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jCalendarioFocusGained(evt);
            }
        });
        jCalendario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCalendarioMouseClicked(evt);
            }
        });
        jCalendario.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jCalendarioPropertyChange(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nome do Evento:");

        txtEvento.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtEvento.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jScrollPane1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        txtDescricao.setColumns(20);
        txtDescricao.setRows(5);
        jScrollPane1.setViewportView(txtDescricao);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Lista de eventos em aberto:");

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/pagendaescolar/icones/create.png"))); // NOI18N
        btnAdicionar.setToolTipText("INSERIR");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/pagendaescolar/icones/update.png"))); // NOI18N
        btnAlterar.setToolTipText("ALTERAR");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/pagendaescolar/icones/delete.png"))); // NOI18N
        btnExcluir.setToolTipText("EXCLUIR");

        tblEventos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Evento", "Matéria", "Descrição", "Dt Limite"
            }
        ));
        tblEventos.setShowGrid(true);
        tblEventos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEventosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEventos);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Matéria:");

        comboMateria.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        comboMateria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Matemática", "Português", "Inglês", "História", "Geografia", "Artes", "Ciências" }));

        lblUsuario.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblUsuario.setText("NOME DO USUARIO");

        jDataLimite.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDataLimitePropertyChange(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Data limite:");

        lblDataLimite.setForeground(new java.awt.Color(255, 255, 255));
        lblDataLimite.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDataLimite.setText("Limite");

        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aberto", "Fechado" }));

        lblDataCalendario.setAlignment(java.awt.Label.CENTER);
        lblDataCalendario.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        lblDataCalendario.setForeground(new java.awt.Color(255, 255, 255));
        lblDataCalendario.setText("DATA CALE");

        lblDataHoje.setForeground(new java.awt.Color(255, 255, 255));
        lblDataHoje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDataHoje.setText("HOJE");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Id");

        txtId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtId.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(lblDataHoje, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100)
                        .addComponent(lblDataCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(57, 57, 57)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(lblDataLimite)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(txtEvento, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(comboMateria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDataLimite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDataLimite, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane2)))
                    .addComponent(jCalendario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDataCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDataLimite))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDataHoje)))
                .addContainerGap())
        );

        jMenu1.setText("Gestão");

        jMenuItem1.setText("Usuários");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Matérias");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Sobre");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1556, 595));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jCalendarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCalendarioMouseClicked

        atualiza_datacalendario();

    }//GEN-LAST:event_jCalendarioMouseClicked

    private void jCalendarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCalendarioFocusGained

        atualiza_datacalendario();

    }//GEN-LAST:event_jCalendarioFocusGained

    private void jCalendarioPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jCalendarioPropertyChange

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String data = sdf.format(jCalendario.getDate());

        lblDataCalendario.setText(data);
    }//GEN-LAST:event_jCalendarioPropertyChange

    private void jDataLimitePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDataLimitePropertyChange

        System.out.println(jDataLimite.getDate());

        if (jDataLimite.getDate() == null) {

            lblDataLimite.setText("Limite");

        } else {

            SimpleDateFormat sdfdatalimite = new SimpleDateFormat("dd/MM/yyyy");

            String datalimite = sdfdatalimite.format(jDataLimite.getDate());

            lblDataLimite.setText(datalimite);
        }
    }//GEN-LAST:event_jDataLimitePropertyChange

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionar_evento();
        carregar_tabela_eventos();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void tblEventosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEventosMouseClicked
        setar_campos();
        pesquisar_evento_id();
    }//GEN-LAST:event_tblEventosMouseClicked

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        alterar_evento();
        carregar_tabela_eventos();
    }//GEN-LAST:event_btnAlterarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(telaPrincipalAEscolar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(telaPrincipalAEscolar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(telaPrincipalAEscolar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(telaPrincipalAEscolar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new telaPrincipalAEscolar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JComboBox<String> comboMateria;
    private javax.swing.JComboBox<String> comboStatus;
    private com.toedter.calendar.JCalendar jCalendario;
    private com.toedter.calendar.JDateChooser jDataLimite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private java.awt.Label lblDataCalendario;
    private javax.swing.JLabel lblDataHoje;
    private javax.swing.JLabel lblDataLimite;
    public static java.awt.Label lblUsuario;
    private javax.swing.JTable tblEventos;
    private javax.swing.JTextArea txtDescricao;
    private javax.swing.JTextField txtEvento;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
}
