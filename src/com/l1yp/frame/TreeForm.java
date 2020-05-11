package com.l1yp.frame;

import com.l1yp.util.HexUtil;
import com.l1yp.util.PbParser;
import com.l1yp.util.PbParser.BaseElem;
import com.l1yp.util.PbParser.BytesElem;
import com.l1yp.util.PbParser.Fixed32Elem;
import com.l1yp.util.PbParser.Fixed64Elem;
import com.l1yp.util.PbParser.LengthElem;
import com.l1yp.util.PbParser.RootElem;
import com.l1yp.util.PbParser.VarintElem;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * @Author Lyp
 * @Date 2020/5/8
 * @Email l1yp@qq.com
 */
public class TreeForm extends JFrame {

    DefaultMutableTreeNode root;

    JTree tree;

    public TreeForm(){
        setBounds(0, 0, 1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(1, 1));
        setLocationRelativeTo(null);


        //加滚动条--------------------------------------------------------
        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane);
        //加滚动条-----------------------------------------------------

        initModel();

        tree = new JTree(root);


        expandAll();


        CustomRender renderer = new CustomRender();
        renderer.setIcon(null);
        renderer.setDisabledIcon(null);
        renderer.setEnabled(false);
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);


        tree.setRowHeight(20);
        tree.setCellRenderer(renderer);

        scrollPane.setViewportView(tree);
    }

    private void initModel(){
        PbParser parser = new PbParser();
        byte[] bytes = HexUtil.hex2bin("08C9EAE5021001180220A09C014800880100A80600B20600BA060012FA0A82E209F50A0A92010A28312D322D30613333643966662D333335322D346662342D393065342D666136306132383738616639100818012213080110C9EAE5021A034C797020C9EAE5022A00280130A4EC013A08372E362E33353935400248E7DAD4C6E62C50E6A4D6C6E62C58E6A4D6C6E62C60E7DAD4C6E62C7A1942170A07313131313131311A0A323232323232323232322801980100A001000A9C010A28312D322D39313862663562632D333233322D343430362D396639622D613666346666336333303335100818012213080110C9EAE5021A034C797020C9EAE5022A00280130A4EC013A08372E362E33353935400248B7EFCEC6E62C50D999D0C6E62C58D999D0C6E62C60B7EFCEC6E62C7A2342210A0B33333333333333333333331A10333332323232323232323232323232322801980100A001000A8D010A28312D322D39393336363962302D383535352D343733322D616632352D333031636635313461653463100818012213080110C9EAE5021A034C797020C9EAE5022A00280130A4EC013A08372E362E3335393540024888E1C6C6E62C50C297C8C6E62C58C297C8C6E62C6088E1C6C6E62C7A1442120A033132331A093132333132333132332801980100A001000AE9020A28312D312D61616665333739312D363832342D343362312D613930372D626631653262613531656531100818012220080210AB95D6AA071A0A3139363835343033333120C8A38A85022A04654C6962280130A4EC013A08372E362E33353935400148888BD5A8D72C508CEE9EAAD72C588CEE9EAAD72C60BBE29EAAD72C7AE20142DF0122DA010A4A687474703A2F2F7368702E717069632E636E2F636F6C6C6563746F722F353836323732392F39666237613632322D376562632D343462662D616361622D6161386430666235396432612F1210B07539403B2BE0B3638220E0BD81C35F2220423037353339343033423242453042333633383232304530424438314333354630DC0138C8024096F1970248005220080210AB95D6AA071A0A3139363835343033333120C8A38A85022A04654C69625A29303431352F39666237613632322D376562632D343462662D616361622D6161386430666235396432612801980100A001000ACB010A28312D342D66366133333230652D646335372D343633662D623536642D33626638383365386433333010081801220F080110C9EAE5021A044C3179702000280130A5EC013A07372E362E34363640044881B394B0C62C50E5B494B0C62C58E5B494B0C62C6081B394B0C62C7A5742550A001A4D687474703A2F2F7371696D672E71712E636F6D2F2F71715F70726F647563745F6F7065726174696F6E732F746971712F686F6E6573742D7361792F696D672F6267322D3037623365382E706E6728013A00980100A001000AEE020A28312D312D35363531313038332D653862642D343062372D623739612D64663933393535393964626610061801221E080110C7BFD4F4071A06E8AF97E88CB520C7BFD4F4072A06E8AF97E88CB5280130A3EC013A0A35352E392E3139383736400148EFF0C4F6E82B50DEFAC4F6E82B58DEFAC4F6E82B60EFF0C4F6E82B7AEA0132E7010A8501080210C9EAE50218EC01224534393735353930306137383864323965623066666630306338623866663832352F34663237343461632D383639632D346362652D383339342D3136306639353738363637332A06322E646F637830DFD4013A10981C2038729FA5B35D9B18378D271D5842140687C616D0A07244FCC5E738EFB71CDA486A9FDD125D080110C7BFD4F4071803224534393735353930306639356230396466346465333565613163373833633336385F30343065643831642D373833342D343634372D626163322D3165623565646337626436312A06322E646F637830DFD401A00100100618D88F0D");
        // root = parser.parse(null, byets, 0, byets.length);
        RootElem rootElem = parser.parse(bytes);
        System.out.println("rootElem = " + rootElem);
        root = new DefaultMutableTreeNode(rootElem);
        transferToTree(rootElem, root);
    }

    private void transferToTree(BaseElem baseElem, DefaultMutableTreeNode parentNode){
        List<BaseElem> children = baseElem.getChildren();
        for (BaseElem child : children) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(child, !child.isLeaf());
            parentNode.add(node);
            if (!child.isLeaf()){
                transferToTree(child, node);
            }
        }

    }

    private void expandAll() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    private static class CustomRender extends DefaultTreeCellRenderer{

        private static final ImageIcon textIcon = new ImageIcon(ClassLoader.getSystemResource("letter-t.png"));
        private static final ImageIcon dwIcon = new ImageIcon(ClassLoader.getSystemResource("letter-d.png"));
        private static final ImageIcon qwIcon = new ImageIcon(ClassLoader.getSystemResource("letter-q.png"));
        private static final ImageIcon hexIcon = new ImageIcon(ClassLoader.getSystemResource("letter-h.png"));
        private static final ImageIcon vIcon = new ImageIcon(ClassLoader.getSystemResource("letter-v.png"));

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            JComponent component = (JComponent)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (leaf){
                if (value instanceof DefaultMutableTreeNode node){
                    if (node.getUserObject() instanceof VarintElem){
                        component.setBackground(Color.decode(sel ? "#0078d7" : "#ffffff"));
                        component.setForeground(Color.decode(sel ? "#ffffff" : "#000000"));
                        setIcon(vIcon);
                    }else if (node.getUserObject() instanceof LengthElem){
                        component.setBackground(Color.decode(sel ? "#0078d7" : "#ffffff"));
                        component.setForeground(Color.decode(sel ? "#ffffff" : "#5830E0"));
                    }else if (node.getUserObject() instanceof BytesElem elem){
                        component.setBackground(Color.decode(sel ? "#0078d7" : "#ffffff"));
                        Boolean printable = elem.getPrintable();
                        if (printable == null){
                            component.setForeground(Color.decode(sel ? "#ffffff" : "#bc6710"));
                            setIcon(hexIcon);
                        }else {
                            component.setForeground(Color.decode(sel ? "#ffffff" : (printable ? "#bc6710" : "#ff00ff")));
                            setIcon(printable ? textIcon : hexIcon);
                        }
                    }else if (node.getUserObject() instanceof Fixed32Elem){
                        setIcon(dwIcon);
                    }else if (node.getUserObject() instanceof Fixed64Elem){
                        setIcon(qwIcon);
                    }
                }
                component.setFont(new Font("微软雅黑", Font.BOLD, 14));
            }else {
                component.setBackground(Color.decode(sel ? "#0078d7" : "#ffffff"));
                component.setForeground(Color.decode(sel ? "#ffffff" : "#000000"));
                component.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            }


            Border border = BorderFactory.createEmptyBorder(20, 3, 20, 5);
            component.setBorder(border);
            return component;
        }
    }

}
