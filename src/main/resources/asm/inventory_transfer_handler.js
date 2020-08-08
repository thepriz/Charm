function initializeCoreMod() {
    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var IincInsnNode = Java.type('org.objectweb.asm.tree.IincInsnNode');
    var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    return {
        'quark_inventory_transfer_handler': {
            target: {
                'type': 'METHOD',
                'class': 'vazkii.quark.base.handler.InventoryTransferHandler',
                'methodName': 'accepts', // accepts
                'methodDesc': '(Lnet/minecraft/inventory/container/Container;Lnet/minecraft/entity/player/PlayerEntity;)Z'
            },
            transformer: function(method) {
                var newInstructions = new InsnList();
                var instruction = method.instructions.get(0);
                var label = new LabelNode();

                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "quarkInventoryTransferHook", "(Lnet/minecraft/inventory/container/Container;)Z", ASM.MethodType.STATIC));
                newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                newInstructions.add(new InsnNode(Opcodes.ICONST_1));
                newInstructions.add(new InsnNode(Opcodes.IRETURN));
                newInstructions.add(label);

                method.instructions.insert(instruction, newInstructions);

                print("[Charm ASM] (InventoryTransferHandler) Patched InventoryTransferHandler");
                return method;
            }
        }
    }
}